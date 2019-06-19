/*
 * Copyright (C) 2019 Stefan Zeller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.docksnet.rgraph.toolwindow;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class ReferenceToolWindow implements ToolWindowFactory {

    public static final String ID = "Package References";

    private MyToolWindow samePackageReferences = new MyToolWindow();
    private MyToolWindow sameHierarchieReferences = new MyToolWindow();
    private MyToolWindow otherHierarchieReferences = new MyToolWindow();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

        {
            Content content = contentFactory.createContent(this.samePackageReferences.getContent(), "Same Package References", false);
            toolWindow.getContentManager().addContent(content);
        }

        {
            Content content = contentFactory.createContent(this.sameHierarchieReferences.getContent(), "Same Hierarchy References", false);
            toolWindow.getContentManager().addContent(content);
        }

        {
            Content content = contentFactory.createContent(this.otherHierarchieReferences.getContent(), "Other Hierarchy References", false);
            toolWindow.getContentManager().addContent(content);
        }

        ServiceManager.getService(project, ProjectService.class)
                .setSamePackageReferences(this.samePackageReferences)
                .setSameHierarchieReferences(this.sameHierarchieReferences)
                .setOtherHierarchieReferences(this.otherHierarchieReferences);
    }

    @Override
    public void init(ToolWindow window) {

    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public boolean isDoNotActivateOnStart() {
        return false;
    }
}
