import React from 'react';
import { InputNumber } from 'primereact/inputnumber';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import classnames from 'classnames';
import { Panel } from 'primereact/panel';
import 'primereact/resources/themes/tailwind-light/theme.css'
import 'primereact/resources/primereact.min.css'
import 'primeicons/primeicons.css'
import { REST_API } from '../App';
import './_.scss';

class InputComponents extends React.Component {
    constructor(props) {
        super(props);
        this.state = { };
    }

    validateX() {
        const xNumber = Number(this.props.x);
        return !isNaN(xNumber) && xNumber >= -3 && xNumber < 3;
    }

    change_r(r) {
        const rNumber = Number(r);
        if (r != "" && !(rNumber == 1 || rNumber == 2 || rNumber == 3)) {
            this.setState({ errorR: true });
        } else {
            this.setState({ errorR: false });
            if (this.props.r != "")
                this.props.change_prev_r(this.props.r);
            this.props.change_r(r);
        }
    }
 
    save() {
        const { x, y, r } = this.props;
        let error;
        if (!this.validateX()) {
            error = true;
            this.setState({ errorX: true });
        } else {
            this.setState({ errorX: false });
        }

        if (!error) {
            const apiUrl = `${REST_API}/calc/results`;
            fetch(apiUrl, {
                mode: 'cors',
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ x, y, r })
            }).then(() => { this.props.change_saved(new Date().getTime()) });
        }
    }

    render() {
        const { errorX, errorR } = this.state;
        return (
            <Panel
                pt={{
                    content: {
                        className: "panel widgetPanel widgetPanelCol1"
                    }
                }}
            >
                <Panel
                    pt={{
                        content: {
                            className: "inputPanel"
                        }
                    }}
                >
                    <div className="inputContainer">
                        <label htmlFor="x" className="label">X</label>
                        <InputText
                            id="x" value={this.props.x}
                            className="input"
                            onChange={(e) => this.props.change_x(e.target.value)}
                            aria-describedby="x-help"
                        />
                    </div>
                    <small id="x-help"
                        className={classnames(
                            errorX ? 'smallError' : null
                        )}
                    >
                        Enter X from -3 to 3
                    </small>
                    <div className="inputContainer">
                        <label htmlFor="y" className="label">Y</label>
                        <InputNumber
                            id="y"
                            className="p-inputnumber-horizontal inputNumber"
                            value={this.props.y}
                            onValueChange={(e) => this.props.change_y(e.value)} 
                            mode="decimal" 
                            showButtons 
                            min={-3} max={5} step={0.5}
                            buttonLayout="horizontal"
                            incrementButtonIcon="pi pi-plus" decrementButtonIcon="pi pi-minus"
                            aria-describedby="y-help"
                        />
                    </div>
                    <small id="y-help">Enter Y from -3 to 5</small>
                    <div className="inputContainer">
                        <label htmlFor="r" className="label">Radius</label>
                        <InputText
                            id="r" value={this.props.r}
                            className="input"
                            onChange={(e) => this.change_r(e.target.value) }
                            aria-describedby="r-help"
                        />
                    </div>
                    <small id="r-help">Enter R from 1 to 3</small>
                </Panel>
                <div className='toolbar'>
                    <Button label="Save" icon="pi pi-check" onClick={() => this.save()} className="saveButton" />
                </div>
            </Panel>
        );
    }
}

export default InputComponents;