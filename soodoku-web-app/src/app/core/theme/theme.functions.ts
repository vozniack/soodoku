import { Theme } from './theme.const';

export function mapTheme(value: string | null | undefined): Theme {
  switch (value) {
    case Theme.LIGHT:
      return Theme.LIGHT;

    case Theme.DARK:
      return Theme.DARK;

    default:
      return Theme.LIGHT;
  }
}
