/*
 * Copyright (C) 2009-2010 hidetake.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hidetake.util.oauth.config;

import java.util.List;

import javax.servlet.FilterConfig;

import org.hidetake.util.oauth.ValidationEventListener;
import org.hidetake.util.oauth.model.OpenSocialAccessor;

/**
 * Servlet filter initialization context.
 * 
 * @author hidetake.org
 *
 */
public interface InitContext
{

	public FilterConfig getFilterConfig();
	
	public OpenSocialAccessor getOpenSocialAccessor();
	
	public List<ValidationEventListener> getValidationEventListeners();

}
