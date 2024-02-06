import { connect } from 'react-redux';
import Results from '../results';
import mapStateToProps from '../../store/mapStateToProps';
import mapDispatchToProps from '../../store/mapDispatchToProps';

const RESULTS_W = connect(mapStateToProps("Results"), mapDispatchToProps("Results"))(Results);

export default RESULTS_W;