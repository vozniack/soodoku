import {Action, ActionReducer} from '@ngrx/store';

export function persistState<S, A extends Action = Action>(reducer: ActionReducer<S, A>) {

  const localStorageKey = 'soodoku-storage';

  let onInit = true;

  return function (state: S, action: A): S {
    const nextState = reducer(state, action);

    if (onInit) {
      onInit = false;

      const savedState = JSON.parse(localStorage.getItem(localStorageKey) as string);

      return {
        ...nextState,
        ...savedState
      };
    }

    localStorage.setItem(localStorageKey, JSON.stringify(nextState));

    return nextState;
  };
}
