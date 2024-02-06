import ACTION_CHANGE_SAVED from '../actions/change_saved';

function action_change_saved(value) {
	return {
		type: ACTION_CHANGE_SAVED,
		saved: value
	};
}

export default action_change_saved;