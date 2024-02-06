import { bindActionCreators } from 'redux';
import change_x from './actionCreators/change_x';
import change_y from './actionCreators/change_y';
import change_r from './actionCreators/change_r';
import change_prev_r from './actionCreators/change_prev_r';
import change_saved from './actionCreators/change_saved';

function mapDispatchToProps(component) {
	switch (component) {
		case "Input": return function (dispatch) {
			return {
				change_x: bindActionCreators(change_x, dispatch),
				change_y: bindActionCreators(change_y, dispatch),
				change_r: bindActionCreators(change_r, dispatch),
				change_prev_r: bindActionCreators(change_prev_r, dispatch),
				change_saved: bindActionCreators(change_saved, dispatch),
			};
		};
		case "Canvas": return function (dispatch) {
			return {
				change_saved: bindActionCreators(change_saved, dispatch),
			};
		};
		default: return undefined;
	}
}

export default mapDispatchToProps;