package com.alanwalker.util;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {
	
	public static void main(String[] args) {
		// Character
//		TexturePacker.process(
//				"resource/unpacked", 
//				"resource/character/alan", 
//				"alan");
		
		// Button
		TexturePacker.process(
				"resource/ui/button/unpacked", 
				"resource/ui/button/", 
				"button");
	}

}
