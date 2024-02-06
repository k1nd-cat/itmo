import ACTION_CHANGE_PREV_R from '../actions/change_prev_r';

function action_change_prev_r(value) {
	return {
		type: ACTION_CHANGE_PREV_R,
		prev_r: value
	};
}

export default action_change_prev_r;