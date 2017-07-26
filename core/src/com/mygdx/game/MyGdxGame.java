package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.badlogic.gdx.*;
import org.json.*;
import org.apache.*;
import org.apache.commons.io.IOUtils;

import org.json.simple.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor, Screen {

	public static final int VIRTUAL_WIDTH = 960;
	public static final int VIRTUAL_HEIGHT = 768;
	private World world; // do body editor

	static Color color;
	
	Sprite sprite;
	BodyEditorLoader loader;

	Region silesia;
	Region tim;
	Region pomerania;
	
	OrthographicCamera cam;
	Viewport viewport;
	ShapeRenderer shapeBatch;
	SpriteBatch batch;
	Vector2 lastTouch;
	
	boolean czyPauza;
	Sprite mapSprite; // do kamery
	float rotationSpeed; // do kamery
	long startTime;
	float w, h, gx, gy;
	Vector2 coords;
	Vector3 touchPos;
	Color sda;

	Sprite silesiaSprite;
	
	static float a;
	JSONObject jsonObject;
	JSONParser parser;
	Object obj;
	@Override
	public void create() {
		Box2D.init();
		rotationSpeed = 0.5f; // do kamery

		batch = new SpriteBatch();
		
		
		parser = new JSONParser();
	    jsonObject = null;

		
		pomerania = new Region();
		//pomerania.addingSprites();
		Collection<Region> Provinces = new ArrayList<Region>();
		Provinces.add(pomerania);
		
		touchPos = new Vector3();
		world = new World(new Vector2(0, -10), true); // do editora
		startTime = System.currentTimeMillis();
		mapSprite = new Sprite(new Texture(Gdx.files.internal("Europe.png"))); // do kamery
		mapSprite.setPosition(0, 0); // do kamery
		mapSprite.setSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT); // do kamery

		silesiaSprite = new Sprite(new Texture(Gdx.files.internal("silesia.png")));
		silesiaSprite.setPosition(0, 0);
		
		Region.addSprites(); // adding sprites of regions
		Region.startTimer();
		
		w = Gdx.graphics.getWidth();// do kamery
		h = Gdx.graphics.getHeight();// do kamery
		cam = new OrthographicCamera(960, 768 * (h / w)); // CAM CENTER ?
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0); // CAM - START POSITION
		cam.update();
		lastTouch = new Vector2();
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(this);
		Gdx.input.setInputProcessor(im);

		shapeBatch = new ShapeRenderer();

		silesia = new Region();
		silesia.startPopulation = 4.635f;
		silesia.nationality = "Poland";
		silesia.culture = "Polish - Silesian";
		silesia.urbanizationLevel = 7;
		silesia.montlyTaxes = silesia.urbanizationLevel * silesia.startPopulation;

		czyPauza = false; // PAUZA
	}

	@Override
	public void render() {
		if (!czyPauza) {
			handleInput(); //

			cam.update(); // do kamery
			batch.setProjectionMatrix(cam.combined); // wazne
			if (Gdx.input.isTouched()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0); // when the screen is touched
				cam.unproject(touchPos); // calibrates the input to your camera's dimenstions
				if (touchPos.x > silesiaSprite.getX() && touchPos.x < silesiaSprite.getX() + silesiaSprite.getWidth()) {
					if (touchPos.y > silesiaSprite.getY()
							&& touchPos.y < silesiaSprite.getY() + silesiaSprite.getHeight()) {
						// clicked on sprite
						// do something that vanish the object clicked
						Texture texture = silesiaSprite.getTexture();

						int textureLocalX = (int) (touchPos.x -silesiaSprite.getX());

						int textureLocalY = (int) (silesiaSprite.getHeight() -touchPos.y + silesiaSprite.getY());
						if (!texture.getTextureData().isPrepared()) {
							texture.getTextureData().prepare();
						}
						Pixmap pixmap = texture.getTextureData().consumePixmap();
						color = new Color(pixmap.getPixel(textureLocalX, textureLocalY));

						String tekst = (color.a <= 0.01f) ? ("Nie trafilem w silesie") : ("Trafilem "
								+ "w silesie");
						float alfa = (color.a <= 0.01f) ? 1.0f : 0.8f;
						silesiaSprite.setAlpha(alfa);
						System.out.println(tekst + "  "  +color.a);
					}
				}
			}
			//parser = new JSONParser();

			
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			silesia.populationGrowth(silesia.startPopulation, silesia.urbanizationLevel);
			silesia.population();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			czyPauza = !czyPauza;
		}
		
		
		JSONObject jsonObject = null;

		try {
			
			Object object = parser.parse(new FileReader("E:\\Provinces.json"));         
			 JSONArray  jsonArr = (JSONArray) object;   // Getting c
			 JSONObject jsonObj = (JSONObject) jsonArr.get(0); 
		//	JSONArray array = (JSONArray) parser.parse(new FileReader("E:\\Provinces.json"));         
	      //  jsonObject = array.get(index)
			
		//Object obj = parser.parse(new FileReader("E:\\Provinces.json"));
			//JSONArray json = (JSONArray) obj;

         //  JSONObject jsonObject = (JSONObject) obj;
        //   System.out.println(jsonObject);
           /*
            JSONArray jsonArray = (JSONArray) jsonObject.get("Provinces");
            Iterator<String> iterator = jsonArray.iterator();
            while(iterator.hasNext()) {
            	System.out.println(iterator.next());
            }
            
            */
        //    String name = (String) jsonObject.get("Provinces");
        //    System.out.println(name);

            //long age = (Long) jsonObject.get("TextureName");
            //System.out.println(age);

            // loop array
            /*
            JSONArray msg = (JSONArray) jsonObject.get("Provinces");
            Iterator<String> iterator = msg.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        
		}
	
            
		batch.begin();
		silesiaSprite.draw(batch);
		
		Region.drawSprites(batch);

		batch.end();
	}


	public void handleInput() { // CAM POSITIONING AND ZOOMING

		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
			cam.zoom += 0.05;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.X)) {
			cam.zoom -= 0.05;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			cam.translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			cam.translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			cam.translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			cam.translate(0, 3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, 900 / cam.viewportWidth);

	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = 500f;
		cam.viewportHeight = 500f * height / width;
		cam.update();
	}

	@Override
	public void dispose() {
		mapSprite.getTexture().dispose();
		batch.dispose();
		world.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0); // when the screen is touched
		cam.unproject(touchPos);
		float myTouchX = touchPos.x;
		float myTouchY = touchPos.y;
		// System.out.println("My X: " + myTouchX + ", My Y: " + myTouchY);

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override // CAM POSITIONING MOUSE BY DRAGGING
	public boolean touchDragged(int x, int y, int pointer) {
		float z = Gdx.input.getDeltaX();
		float c = Gdx.input.getDeltaY();
		cam.position.add(-z, c, 0);

		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		// System.out.println("X: " + touchPos.x + ", Y: " + touchPos.y );

		return false;
	}

	@Override // CAM ZOOMING BY MOUSE'S WHEEL
	public boolean scrolled(int amount) {
		if (amount == 1) {
			cam.zoom += 0.09;
		} else if (amount == -1) {
			cam.zoom -= 0.09;

		}
		return false;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

}
