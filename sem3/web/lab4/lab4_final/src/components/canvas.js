import React from 'react';
import { drawCanvas, drawPointByCursor } from './util/draw';
import classnames from 'classnames';
import { Panel } from 'primereact/panel';
import { REST_API } from '../App';

class Canvas extends React.Component {
    componentDidMount() {
		this.draw();
	}

	componentDidUpdate() {
        if (this.props.r != "")
		    this.draw();
	}

    draw() {
        const canvas = document.getElementById("canvas");
        const ctx = canvas.getContext('2d');
        drawCanvas(ctx, this.props.r);
    }

    save(x, y, r) {
		const apiUrl = `${REST_API}/calc/results`;
		fetch(apiUrl, {
			mode: 'cors',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ x, y, r })
		}).then(() => { this.props.change_saved(new Date().getTime()) });
    }

    onMouseDown(e) {
        const xy = drawPointByCursor(e, this.props.r);
        this.save(xy.x, xy.y, this.props.r);
    }

    render() {
        return (
            <Panel
                pt={{
                    content: {
                        className: "panel widgetPanel widgetPanelCol1"
                    }
                }}
            >
                <canvas
                    id="canvas"
                    width="340px"
                    height="340px"
                    onMouseDown={(e) => this.onMouseDown(e)}
                />
            </Panel>
        );
    }
}

export default Canvas;