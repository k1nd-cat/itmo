import React from 'react';
import { Provider } from 'react-redux';
import { Button } from 'primereact/button';
import store from '../store/store';
import { useNavigate } from 'react-router-dom';
import INPUT_W from '../components/wrapComponents/input_w';
import { REST_API } from '../App';
import CANVAS_W from '../components/wrapComponents/canvas_w';
import RESULTS_W from '../components/wrapComponents/results_w';
import './_.scss';

export default function MainPage() {

    const navigate = useNavigate();

    const token = localStorage.getItem("token");
    if (!token) {
        setTimeout(() => navigate('..', { replace: true }), 10);
    } else {
		const apiUrl = `${REST_API}/user/check`;
		fetch(apiUrl, {
			method: "POST",
            headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ token })
		})
			.then((response) => response.json())
			.then((data) => {
                const { result } = data;
                if (!result) {
                    localStorage.removeItem("token");
                    navigate('..', { replace: true });
                }
            });
    }

    const doLogout = () => {
		const apiUrl = `${REST_API}/user/logout`;
		fetch(apiUrl, {
			method: "POST",
            headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ token })
		})
			.then(() => {
                localStorage.removeItem("token");
                navigate('..', { replace: true });
            });
    }

    return (
        <>
            <div className="logout_toolbar--container">
                <div className="panel logout_toolbar">
                    <span className='title'>Calculation area</span>
                    <Button label="Logout" icon="pi pi-sign-out" onClick={doLogout} className="logoutButton" iconPos="right" />
                </div>
            </div>
            <div class="mainPanel--container">
                <div
                    className="mainPanel"
                >
                    <Provider store={store}>
                        <CANVAS_W />
                        <INPUT_W />
                        <RESULTS_W />
                    </Provider>
                </div>
            </div>
        </>
    )
}