import React, { useState, useEffect } from 'react';
import { Panel } from 'primereact/panel';
import 'primereact/resources/themes/tailwind-light/theme.css'
import 'primereact/resources/primereact.min.css'
import 'primeicons/primeicons.css'
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { REST_API } from '../App';
import { drawPointByCoords } from './util/draw';
import './_.scss';

class Results extends React.Component {
    constructor(props) {
        super(props);
        this.state = { saved: props.saved || 0, r: props.r || 2 };
    }

    componentDidMount() {
        this.getResults();
    }

    shouldComponentUpdate(nextProps) {
        const { saved, results } = this.state;
        const shouldUpdate = !results || nextProps.saved !== saved;
        const shouldDrawPoints = nextProps.r !== this.props.r;
        if (!!results && shouldDrawPoints && !shouldUpdate) {
            const refreshedResults = results;
            // const refreshedResults = results.map((item) => {
            //         return { x: item.x * nextProps.r / item.r, y: item.y * nextProps.r / item.r, r: item.r };
            // });
            this.drawPoints(refreshedResults);
        }
        return shouldUpdate;
    }

    componentDidUpdate() {
        this.getResults();
    }

    async getResults() {
        const apiUrl = `${REST_API}/calc/results`;
        const response = await fetch(apiUrl, {
            mode: 'cors',
            headers: { 'Content-Type': 'application/json' },
        });
        const data = await response.json();
        this.drawPoints(data);
        this.setState({ results: data, saved: this.props.saved });
    }

    drawPoints(results) {
        setTimeout(() => {
            const canvas = document.getElementById("canvas");
            const ctx = canvas.getContext('2d');
            results.forEach((item) => drawPointByCoords(ctx, item.x, item.y, item.r));
        }, 50);
    }

    number = (val) => {
        if (!val) return val;
        val = String(val);
        if (val.indexOf('.') !== -1 && val.substring(val.indexOf('.') + 1) > 3) {
            return Number(val).toFixed(3);
        } else {
            return val;
        }
    }

    render() {
        const { results } = this.state;
        return ( 
            <Panel
                pt={{
                    root: { className: 'panel widgetPanel resultsPanelRoot' },
                    content: { className: 'resultsPanel' }
                }}
            >
                <DataTable
                    value={results} tableStyle={{ minWidth: '360px' }}
                    pt={{
                        header: { className: "transparrent" },
                        headerRow: { className: "transparrent header" },
                        bodyRow: { className: "transparrent" }
                    }}
                    scrollable scrollHeight="440px"
                >
                    <Column field="x" header="X" body={(record) => this.number(record.x)} className="w18"></Column>
                    <Column field="y" header="Y" body={(record) => this.number(record.y)} className="w18"></Column>
                    <Column field="r" header="Radius" className="w18"></Column>
                    <Column field="result" header="Result" body={(record) => record.result ? 'In area' : 'Out of area'} className="w46"></Column>
                </DataTable>
            </Panel>
        );
    }
}

export default Results;