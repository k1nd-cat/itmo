import { connect } from 'react-redux';
import Canvas from '../canvas';
import mapStateToProps from '../../store/mapStateToProps';
import mapDispatchToProps from '../../store/mapDispatchToProps';

const CANVAS_W = connect(mapStateToProps("Canvas"), mapDispatchToProps("Canvas"))(Canvas);

export default CANVAS_W;