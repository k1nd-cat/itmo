import { connect } from 'react-redux';
import Input from '../input';
import mapStateToProps from '../../store/mapStateToProps';
import mapDispatchToProps from '../../store/mapDispatchToProps';

const INPUT_W = connect(mapStateToProps("Input"), mapDispatchToProps("Input"))(Input);

export default INPUT_W;