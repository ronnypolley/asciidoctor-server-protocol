/*
 * Copyright 2019 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package de.jcup.asp.server.asciidoctorj.provider;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.jruby.AsciiDocDirectoryWalker;
import org.asciidoctor.jruby.DirectoryWalker;

public class AsciiDoctorAttributesProvider extends AbstractAsciiDoctorProvider{
	
	private Map<String, Object> cachedAttributes;

	AsciiDoctorAttributesProvider(ProviderContext context){
		super(context);
	}

	public Map<String, Object> getCachedAttributes() throws IOException {
		if (cachedAttributes == null) {
			cachedAttributes = resolveAttributes(getContext().getBaseDir());
		}
		return cachedAttributes;
	}

	protected Map<String, Object> resolveAttributes(File baseDir) throws IOException {
	    Objects.requireNonNull(baseDir, "Base dir must be set and not null!");
	    if (! baseDir.exists()) {
	        throw new IOException("Base dir does not exist:"+baseDir);
	    }
		Map<String, Object> map = new HashMap<>();
		Set<DocumentHeader> documentIndex = new HashSet<DocumentHeader>();
		DirectoryWalker directoryWalker = new AsciiDocDirectoryWalker(baseDir.getAbsolutePath());

		for (File file : directoryWalker.scan()) {
			documentIndex.add(getContext().getAsciiDoctor().readDocumentHeader(file));
		}
		for (DocumentHeader header : documentIndex) {
			map.putAll(header.getAttributes());
		}
		return map;
	}

	public void reset() {
		cachedAttributes=null;
	}

}
