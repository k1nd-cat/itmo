import ACTION_CHANGE_R from '../actions/change_r';

function action_change_r(value) {
	return {
		type: ACTION_CHANGE_R,
		r: value
	};
}

export default action_change_r;