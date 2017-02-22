/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.UserBrick;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.formulaeditor.datacontainer.DataContainer;

import java.util.List;
import java.util.Map;

public class ShowTextActor extends Actor {

	private int xPosition;
	private int yPosition;
	private int textSize;
	private float textColorRed;
	private float textColorGreen;
	private float textColorBlue;
	private int alpha = 1;

	private UserVariable variableToShow;
	private String variableNameToCompare;
	private String variableValue;

	private Sprite sprite;
	private UserBrick userBrick;
	private BitmapFont font;

	public ShowTextActor(UserVariable userVariable, int xPosition, int yPosition, int textSize, float red, float green,
			float blue, Sprite sprite, UserBrick userBrick) {
		this.variableToShow = userVariable;
		this.variableNameToCompare = variableToShow.getName();
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.textSize = textSize;
		this.textColorRed = red;
		this.textColorGreen = green;
		this.textColorBlue = blue;
		this.sprite = sprite;
		this.userBrick = userBrick;
		init();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		DataContainer dataContainer = ProjectManager.getInstance().getSceneToPlay().getDataContainer();

		List<UserVariable> projectVariableList = dataContainer.getProjectVariables();
		Map<Sprite, List<UserVariable>> spriteVariableMap = dataContainer.getSpriteVariableMap();
		List<UserVariable> spriteVariableList = spriteVariableMap.get(sprite);
		List<UserVariable> userBrickVariableList = dataContainer.getOrCreateVariableListForUserBrick(userBrick);

		drawVariables(projectVariableList, batch);
		drawVariables(spriteVariableList, batch);
		drawVariables(userBrickVariableList, batch);
	}

	private void drawVariables(List<UserVariable> variableList, Batch batch) {
		if (variableList == null) {
			return;
		}

		if (variableToShow.isDummy()) {
			font.draw(batch, variableToShow.getValue().toString(), xPosition, yPosition);
		} else {
			for (UserVariable variable : variableList) {
				if (variable.getName().equals(variableToShow.getName())) {
					variableValue = variable.getValue().toString();
					if (variable.getVisible()) {
						isNumberAndInteger();
						font.draw(batch, variableValue, xPosition, yPosition);
					}
					break;
				}
			}
		}
	}

	private void init() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arial.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = textSize/2;

		parameter.color = new Color(textColorRed/255, textColorGreen/255, textColorBlue/255, alpha);

		font = generator.generateFont(parameter);
		generator.dispose();
	}

	private void isNumberAndInteger() {
		double variableValueIsNumber = 0;

		if (variableValue.matches("-?\\d+(\\.\\d+)?")) {
			variableValueIsNumber = Double.parseDouble(variableValue);
		} else {
			return;
		}

		if (((int) variableValueIsNumber) - variableValueIsNumber == 0) {
			variableValue = Integer.toString((int) variableValueIsNumber);
		} else {
			return;
		}
	}

	public void setPositionX(int xPosition) {
		this.xPosition = xPosition;
	}

	public void setPositionY(int yPosition) {
		this.yPosition = yPosition;
	}

	public String getVariableNameToCompare() {
		return variableNameToCompare;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public UserBrick getUserBrick() {
		return userBrick;
	}
}
