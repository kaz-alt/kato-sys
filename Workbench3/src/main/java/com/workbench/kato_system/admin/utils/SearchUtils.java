package com.workbench.kato_system.admin.utils;

import com.ibm.icu.text.Transliterator;

public class SearchUtils {

  public static String HiraganaToKatakana(String hiragana) {

    Transliterator trans = Transliterator.getInstance("Hiragana-Katakana");

    String katakana = trans.transliterate(hiragana);

    return katakana;
  }

}
