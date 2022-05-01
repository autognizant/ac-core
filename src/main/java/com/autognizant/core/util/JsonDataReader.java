/*
 * Copyright 2022 Autognizant.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.autognizant.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.autognizant.core.dataTypes.JsonWebElement;
import com.google.gson.Gson;

public class JsonDataReader {

	public List<JsonWebElement> getObjectRepositoryData(File jsonFile) {
		Gson gson = new Gson();
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new FileReader(jsonFile));
			JsonWebElement[] elements = gson.fromJson(bufferReader, JsonWebElement[].class);
			return Arrays.asList(elements);
		}catch(FileNotFoundException e) {
			throw new RuntimeException("Json file not found at path : " + jsonFile);
		}finally {
			try { if(bufferReader != null) bufferReader.close();}
			catch (IOException ignore) {}
		}
	}
	
	public List<JsonWebElement> getObjectRepositoryData(InputStream jsonFile) {
		Gson gson = new Gson();
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new InputStreamReader(jsonFile));
			JsonWebElement[] elements = gson.fromJson(bufferReader, JsonWebElement[].class);
			return Arrays.asList(elements);
		}finally {
			try { if(bufferReader != null) bufferReader.close();}
			catch (IOException ignore) {}
		}
	}
}
