package com.gsoster.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public final class SkillButton {

    private SkillButton () {

    }

    private static int counter = 0;

    public static TextButton generateButton(String text, Skin skin) {
       if (text == "") {
           text = "Skill " + counter;
       }
       counter++;
       return new TextButton(text, skin);
    }

}
