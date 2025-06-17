import { Language } from './language.const';

export function initialLanguage(): Language {
  const browserLang = navigator.language.toLowerCase();

  if (browserLang.startsWith('pl')) {
    return Language.POLISH;
  } else if (browserLang.startsWith('en')) {
    return Language.ENGLISH;
  }

  return Language.ENGLISH;
}

export function mapLanguage(value: string | null | undefined): Language {
  switch (value) {
    case Language.POLISH:
      return Language.POLISH;

    case Language.ENGLISH:
      return Language.ENGLISH;

    default:
      return Language.ENGLISH;
  }
}
