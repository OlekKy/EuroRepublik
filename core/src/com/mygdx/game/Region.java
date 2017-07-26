package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Region {
	
	int urbanizationLevel;

	float startPopulation;
	float montlyTaxes;
	float popGrowth;

	String culture;
	String nationality;
	
	static long startTime;
	static long tim;
	
	static SpriteBatch batch;

	static Vector3 touchPos;
	
	static Sprite lesserPolandSprite;
	static Sprite greaterPolandSprite;
	static Sprite mazoviaSprite;
	static Sprite mazuriaSprite;
	static Sprite pomeraniaSprite;
	static Sprite southernBalticSeaSprite;
	static Sprite gdanskBaySprite;
	static Sprite kaliningradSprite;
	static Sprite kurischesHaffSprite;
	static Sprite easterSlovakiaSprite;
	
	Sprite pomorze;

	private Sprite sprajt;
	
	public static void startTimer() {
		startTime = System.currentTimeMillis();
		tim = (System.currentTimeMillis() - startTime)/1000;
	}
	
	public void populationGrowth(float p, int uL) {
		
		popGrowth =( p * uL + 0.001f* ((System.currentTimeMillis() - startTime)/1000))/10000; // Wzrost liczby ludnosci, co sekunde +0.001
		//System.out.println(popGrowth);
	}
	
	public void population() {
		float population = startPopulation + popGrowth;
		//System.out.println(population);
	}
	
	public static void addSprites() {
		
		lesserPolandSprite = new Sprite(new Texture(Gdx.files.internal("lesser_poland.png")));
		lesserPolandSprite.setPosition(368, -17);
		
		greaterPolandSprite = new Sprite(new Texture(Gdx.files.internal("greater_poland.png")));
		greaterPolandSprite.setPosition(-20, 215);
		
		mazoviaSprite = new Sprite(new Texture (Gdx.files.internal("mazovia.png")));
		mazoviaSprite.setPosition(340, 209);
		
		mazuriaSprite = new Sprite(new Texture(Gdx.files.internal("mazuria.png")));
		mazuriaSprite.setPosition(300, 437);
		
		pomeraniaSprite = new Sprite(new Texture(Gdx.files.internal("pomerania.png")));
		pomeraniaSprite.setPosition(-42, 387);
		
		southernBalticSeaSprite = new Sprite(new Texture(Gdx.files.internal("southern_baltic_sea.png")));
		southernBalticSeaSprite.setPosition(-36, 510);
		
		gdanskBaySprite = new Sprite(new Texture(Gdx.files.internal("gdansk_bay.png")));
		gdanskBaySprite.setPosition(254, 599);

		kaliningradSprite = new Sprite(new Texture(Gdx.files.internal("kaliningrad.png")));
		kaliningradSprite.setPosition(346, 620);
		
		kurischesHaffSprite = new Sprite(new Texture(Gdx.files.internal("kurisches_haff.png")));
		kurischesHaffSprite.setPosition(311, 620);
		
		easterSlovakiaSprite = new Sprite(new Texture(Gdx.files.internal("eastern_slovakia.png")));
		easterSlovakiaSprite.setPosition(443, -111);
	}

	public static void drawSprites(SpriteBatch batch) {
		
		lesserPolandSprite.draw(batch);
		greaterPolandSprite.draw(batch);
		mazoviaSprite.draw(batch);
		mazuriaSprite.draw(batch);
		pomeraniaSprite.draw(batch);
		southernBalticSeaSprite.draw(batch);
		gdanskBaySprite.draw(batch);
		kaliningradSprite.draw(batch);
		kurischesHaffSprite.draw(batch);
		easterSlovakiaSprite.draw(batch);
		
	}
	
	public void addingSprites(Sprite sprajt) {
		pomorze = new Sprite(new Texture(Gdx.files.internal("pomerania.png")));
		pomorze.setPosition(-42, 387);

	}
	
	public void setSprajt(Sprite sprajt) {
		this.sprajt = sprajt;
	}
	
	/*
	public void selectionOfRegion (Vector3 touchPos) {
		
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
		
	}
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((culture == null) ? 0 : culture.hashCode());
		result = prime * result + Float.floatToIntBits(montlyTaxes);
		result = prime * result + ((nationality == null) ? 0 : nationality.hashCode());
		result = prime * result + Float.floatToIntBits(popGrowth);
		result = prime * result + Float.floatToIntBits(startPopulation);
		result = prime * result + urbanizationLevel;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Region other = (Region) obj;
		if (culture == null) {
			if (other.culture != null)
				return false;
		} else if (!culture.equals(other.culture))
			return false;
		if (Float.floatToIntBits(montlyTaxes) != Float.floatToIntBits(other.montlyTaxes))
			return false;
		if (nationality == null) {
			if (other.nationality != null)
				return false;
		} else if (!nationality.equals(other.nationality))
			return false;
		if (Float.floatToIntBits(popGrowth) != Float.floatToIntBits(other.popGrowth))
			return false;
		if (Float.floatToIntBits(startPopulation) != Float.floatToIntBits(other.startPopulation))
			return false;
		if (urbanizationLevel != other.urbanizationLevel)
			return false;
		return true;
	}

}
