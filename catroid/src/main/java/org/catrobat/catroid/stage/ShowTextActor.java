/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.UserBrick;
import org.catrobat.catroid.formulaeditor.DataContainer;
import org.catrobat.catroid.formulaeditor.UserVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowTextActor extends Actor {
	private int xPosition;
	private int yPosition;
	private String variableName;
	private String linkedVariableName;
	private float scale = 3f;
	private BitmapFont font;
	String variableValue;

	private UserVariable variableToShow;
	private String variableNameToCompare;
	private String variableValue;
	private int variableValueType;
	ArrayList<String> textLines;


	private Sprite sprite;
	private UserBrick userBrick;
	private Image image;

	public ShowTextActor(UserVariable userVariable, int xPosition, int yPosition, Sprite sprite, UserBrick userBrick) {
		this.variableToShow = userVariable;
		this.variableValue = userVariable.getValue().toString();
		this.variableNameToCompare = variableToShow.getName();
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.sprite = sprite;
		this.userBrick = userBrick;
		init();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		ProjectManager projectManager = ProjectManager.getInstance();
		DataContainer dataContainer = projectManager.getCurrentProject().getDataContainer();

		List<UserVariable> projectVariableList = dataContainer.getProjectVariables();
		Map<Sprite, List<UserVariable>> spriteVariableMap = dataContainer.getSpriteVariableMap();
		List<UserVariable> spriteVariableList = spriteVariableMap.get(sprite);
		List<UserVariable> userBrickVariableList = dataContainer.getOrCreateVariableListForUserBrick(userBrick);

		drawVariables(projectVariableList, batch, parentAlpha);
		drawVariables(spriteVariableList, batch, parentAlpha);
		drawVariables(userBrickVariableList, batch, parentAlpha);
	}

	private void drawVariables(List<UserVariable> variableList, Batch batch, float parentAlpha) {
		if (variableList == null) {
			return;
		}

		if (variableName.equals("No variable set")) {
			font.draw(batch, variableName, xPosition, yPosition);
		} else {
			for (UserVariable variable : variableList) {
				if (variable.getName().equals(variableToShow.getName())) {
					if (variable.getVisible()) {
						image.draw(batch, parentAlpha);
					}
					break;
				}
			}
		}
	}

	private void checkTypeOfValue(String variableValue) {
		double variableValueIsNumber = 0;
		variableValueType = Constants.VARIABLE_VALUE_STRING;

		if (variableValue.matches("-?\\d+(\\.\\d+)?")) {
			variableValueIsNumber = Double.parseDouble(variableValue);
			variableValueType = Constants.VARIABLE_VALUE_FLOAT;
		} else {
			return;
		}

		if (((int) variableValueIsNumber) - variableValueIsNumber == 0) {
			variableValue = Integer.toString((int) variableValueIsNumber);
			variableValueType = Constants.VARIABLE_VALUE_INTEGER;
		} else {
			return;
		}
	}

	private void init() {
		if (variableValueType == Constants.VARIABLE_VALUE_INTEGER) {

		} else if (variableValueType == Constants.VARIABLE_VALUE_FLOAT) {

		} else {
			textLines = formatString(variableValue);
		}

		Texture texture = new Texture(drawTextOnCanvas(textLines));

		image = new Image(texture);
	}

	public static ArrayList<String> formatString(String text) {
		ArrayList<String> lines = new ArrayList<>();

		int cursorPos = 0;
		while (cursorPos + Constants.MAX_VARIABLE_VALUE_LENGTH < text.length()) {
			String newLine = text.substring(cursorPos, cursorPos + Constants.MAX_VARIABLE_VALUE_LENGTH);
			int lastWhitespace = newLine.lastIndexOf(' ');
			if (lastWhitespace < 0) {
				lastWhitespace = Constants.MAX_VARIABLE_VALUE_LENGTH;
			}
			newLine = text.substring(cursorPos, cursorPos + lastWhitespace);
			while (newLine.contains("\n")) {
				String subLine = newLine.substring(0, newLine.indexOf('\n') + 1);
				lines.add(subLine);
				newLine = newLine.replace(subLine, "");
			}
			lines.add(newLine);
			cursorPos += lastWhitespace;
		}
		lines.add(text.substring(cursorPos, text.length()));

		return lines;
	}

	private Pixmap drawTextOnCanvas(ArrayList<String> lines) {
		Paint paint = new Paint();

		paint.setTextSize(Constants.SHOW_VARIABLE_FONT_SIZE);
		int width = 0;
		int height = 30;
		Rect temp = new Rect();
		ArrayList<Integer> lineStartPoints = new ArrayList<>();

		for (String line : lines) {
			height += 3;
			paint.getTextBounds(line, 0, line.length(), temp);
			height += temp.height();
			lineStartPoints.add(temp.width());
			if (width < temp.width()) {
				width = temp.width();
			}
		}
		width += 55;
		if (width < 148) {
			width = 148;
		}

		for (int i = 0; i < lineStartPoints.size(); i++) {
			if (lineStartPoints.get(i) < width) {
				lineStartPoints.set(i, (width - lineStartPoints.get(i)) / 2);
			} else {
				lineStartPoints.set(i, 0);
			}
		}
		float lineHeight = (height - 30) / lines.size();

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		paint.setAntiAlias(true);
		paint.setColor(android.graphics.Color.BLACK);
		Rect rect = new Rect(0, 0, width, height);

		float y = 40;
		paint.setColor(android.graphics.Color.BLACK);
		int i = 0;
		for (String line : lines) {
			canvas.drawText(line, lineStartPoints.get(i), y, paint);
			y += lineHeight;
			i++;
		}
		
		byte[] bytes = stream.toByteArray();
		return new Pixmap(bytes, 0, bytes.length);
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
