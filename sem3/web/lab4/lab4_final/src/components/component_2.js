import React from 'react';
import { InputNumber } from 'primereact/inputnumber';
import 'primereact/resources/themes/tailwind-light/theme.css'
import 'primereact/resources/primereact.min.css'
import 'primeicons/primeicons.css'
import { REST_API } from '../App';
import './_.scss';

class Component_1 extends React.Component {

	componentDidMount() {
		this.refresh();
	}

	componentDidUpdate() {
		this.refresh();
	}

	refresh() {
		const apiUrl = `${REST_API}/calc/results`;
		fetch(apiUrl, {
			mode: 'cors',
			headers: { 'Content-Type': 'application/json' },
		})
		  .then((response) => response.json())
		  .then((data) => console.log('This is your data', data));
	}

	save() {
		const apiUrl = `${REST_API}/calc/results`;
		fetch(apiUrl, {
			mode: 'cors',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ test: 1 })
		})
			.then((response) => response.json())
			.then((data) => console.log('This is result', data));
	}

	handleAction = () => {
		this.props.change_value_2(!this.props.value_2);
		this.save();
	}

	handleChange = (value) => {
		this.props.change_age(value);
	};

	render() {

		console.log('render component 2');

		return (
			<>
				<p>{this.props.value_1 ? "1 Off" : "1 On"}</p>
				<p>{this.props.value_2 ? "Off" : "On"}</p>
				<input type='button' onClick={() => this.handleAction()} value="Button" />
				<br /><br />
				<InputNumber
					className="p-inputnumber-horizontal"
					value={this.props.age}
					onValueChange={(e) => this.handleChange(e.value)} 
					mode="decimal" 
					showButtons 
					min={10} max={30} step={10}
					buttonLayout="horizontal"
					incrementButtonIcon="pi pi-plus" decrementButtonIcon="pi pi-minus"
				/>
			</>
		);
	}
}

export default Component_1;