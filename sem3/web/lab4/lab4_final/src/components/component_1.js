import React from 'react';

class Component_1 extends React.Component {
	handleAction = () => {
		this.props.change_value_1(!this.props.value_1);
	}
	render() {

		console.log('render component 1');

		return (
			<>
				<p>age: {this.props.age}</p>
				<p>{this.props.value_1 ? "Off" : "On"}</p>
				<p>{this.props.value_2 ? "2 Off" : "2 On"}</p>
				<input type='button' onClick={this.handleAction} value="Button" />
			</>
		);
	}
}

export default Component_1;