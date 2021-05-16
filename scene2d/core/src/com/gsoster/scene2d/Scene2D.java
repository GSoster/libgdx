package com.gsoster.scene2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class Scene2D extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;


	private Skin skin;
	private Stage stage;

	@Override
	public void create () {

		skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		//Begin layout
		Table root = new Table();
		stage.addActor(root);
		root.setFillParent(true);


		Table skillsTable = new Table();
		TextButton textButton = new TextButton("Skill1", skin);
		skillsTable.defaults().space(5);
		skillsTable.add(textButton);
		skillsTable.add(SkillButton.generateButton("Skill2", skin));
		skillsTable.add(new TextButton("Skill3", skin));
		skillsTable.row();
		skillsTable.add(new TextButton("Skill4", skin));
		skillsTable.add(new TextButton("Skill5", skin));
		skillsTable.add(new TextButton("Skill6", skin));
		root.add(skillsTable);
		skillsTable.defaults().reset();
		TextButton endTurnBtn = new TextButton("End Turn", skin);
		endTurnBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("You Clicked me!!");
			}
		});
		endTurnBtn.addListener(new FocusListener() {
			@Override
			public boolean handle(Event event) {
				System.out.println("You hovered me!!");
				return super.handle(event);
			}
		});
		root.add(endTurnBtn).fill();

		skin.get(Label.LabelStyle.class).font.getData().markupEnabled = true;
		Label label = new Label("This [RED]is a test[] of the [#579dd8]Color Markup Language[].", skin);
		root.add(label);

		root.row();


	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
	}
	
	@Override
	public void dispose () {
		skin.dispose();
		stage.dispose();
		batch.dispose();
		img.dispose();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
