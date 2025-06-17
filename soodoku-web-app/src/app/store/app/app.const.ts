import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { Language } from '../../core/language/language.const';
import { Theme } from '../../core/theme/theme.const';
import { View } from '../../core/view/view.const';

export const initialViewState: View = View.HOME;

export const initialThemeState: Theme = Theme.LIGHT;

export const initialLanguageState: Language = Language.POLISH;

export const initialBreakpointState: Breakpoint = Breakpoint.XL;
