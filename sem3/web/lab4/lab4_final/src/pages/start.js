import React, { useState } from 'react';
import classnames from 'classnames';
import { Card } from 'primereact/card';
import { Panel } from 'primereact/panel';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { Password } from 'primereact/password';
import { Message } from 'primereact/message';
import { useNavigate } from 'react-router-dom';
import { REST_API } from '../App';
import './_.scss';

export default function StartPage() {

    const navigate = useNavigate();

    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [password2, setPassword2] = useState('');
    const [register, setRegister] = useState(false);
    const [error, setError] = useState(null);

    const doLogin = () => {
		const apiUrl = `${REST_API}/user/login`;
		fetch(apiUrl, {
			method: "POST",
            headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ login, password })
		})
			.then((response) => response.json())
			.then((data) => {
                const { token, error } = data;
                if (!token) {
                    setError(error);
                } else {
                    // put token to local store
                    localStorage.setItem("token", token);
                    navigate('main', { replace: true });
                }
            });
    }

    const startRegister = () => {
        setRegister(true);
        setError(null);
    }

    const doRegister = () => {
        if (!login || login.trim() === 0) {
            setError('Login is empty');
        } else if (!password || password.trim() < 4) {
            setError('Password must have 4 or more symbols');
        } else if (password !== password2) {
            setError('Password and password repeat are not equal');
        } else {
            const apiUrl = `${REST_API}/user/register`;
            fetch(apiUrl, {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ login, password })
            })
                .then((response) => response.json())
                .then((data) => {
                    const { token, error } = data;
                    if (!token) {
                        setError(error);
                    } else {
                        setRegister(false);
                        // put token to local store
                        localStorage.setItem("token", token);
                        navigate('main', { replace: true });
                    }
                });
        }
    }

    const cancelRegister = () => {
        setRegister(false);
        setError(null);
    }

    const footer = (
        <div className="toolbar flex flex-wrap justify-content-end gap-2">
            <Button label="Login" icon="pi pi-check" onClick={doLogin} className="loginButton" style={{ display: !register ? 'inline' : 'none' }} />
            <Button label="Not registered?" icon="pi" severity="secondary" onClick={startRegister} className="loginButton" style={{ display: !register ? 'inline' : 'none' }} />
            <Button label="Register" icon="pi pi-check" onClick={doRegister} className="loginButton" style={{ display: !register ? 'none' : 'inline' }} />
            <Button label="Cancel" icon="pi pi-times" severity="secondary" onClick={cancelRegister} className="loginButton" style={{ display: !register ? 'none' : 'inline' }} />
        </div>
    );

    return (
        <div className="centered">
            <Card
                title="Лабораторная работа №4, вариант 341332"
                subTitle="Трошкин А.Е., группа P3116"
                footer={footer}
                pt={{
                    root: { className: "card--root" },
                    body: { className: "centered" }
                }}
            >
                <Panel
                    pt={{
                        content: { className: "loginPanel" }
                    }}
                >
                    <div className="loginForm">
                        <label htmlFor="login">Username</label>
                        <InputText id="login" value={login} aria-describedby="username-help"
                          onChange={(e) => {
                            setLogin(e.target.value);
                            setError(null);
                          }}
                        />
                        <small id="username-help">Enter your login</small>
                        <label htmlFor="password">Password</label>
                        <Password id="password" value={password} aria-describedby="password-help" feedback={false}
                          onChange={(e) => {
                            setPassword(e.target.value)
                            setError(null);
                          }}
                        />
                        <small id="password-help">Enter your password</small>
                        <div style={{ marginTop: '20px', display: !register ? 'none' : 'block' }}  >
                            <label htmlFor="password2">Repeat password</label>
                            <Password id="password2" value={password2} aria-describedby="password2-help" feedback={false}
                            onChange={(e) => {
                                setPassword2(e.target.value)
                                setError(null);
                            }}
                            />
                            <small id="password-help">Repeat your password</small>
                        </div>
                        <Message severity="error" text={error} style={{ display: !!error ? 'block' : 'none' }} />
                    </div>
                </Panel>
            </Card>
        </div>
    )
}
