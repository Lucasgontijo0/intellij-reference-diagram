/*
 * Copyright (C) 2015 Stefan Zeller
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

package ch.docksnet.rgraph;

import ch.docksnet.utils.PreConditionUtil;
import com.intellij.psi.PsiClass;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.diagram.BaseDiagramProvider;
import com.intellij.diagram.DiagramColorManager;
import com.intellij.diagram.DiagramElementManager;
import com.intellij.diagram.DiagramPresentationModel;
import com.intellij.diagram.DiagramProvider;
import com.intellij.diagram.DiagramVfsResolver;
import com.intellij.diagram.extras.DiagramExtras;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Stefan Zeller
 */
public class ReferenceDiagramProvider extends BaseDiagramProvider<ReferenceElement> {

    public static final String ID = "ReferenceDiagramProvider";
    private DiagramElementManager<ReferenceElement> myElementManager = new ReferenceDiagramElementManager();
    private DiagramVfsResolver<ReferenceElement> myVfsResolver = new ReferenceDiagramVfsResolver();
    private DiagramExtras<ReferenceElement> myExtras = new DiagramExtras<>();
    private DiagramColorManager myColorManager = new ReferenceDiagramColorManager();

    @Pattern("[a-zA-Z0-9_-]*")
    @Override
    public String getID() {
        return ID;
    }

    @Override
    public DiagramElementManager<ReferenceElement> getElementManager() {
        return myElementManager;
    }

    @Override
    public DiagramVfsResolver<ReferenceElement> getVfsResolver() {
        return myVfsResolver;
    }

    @Override
    public String getPresentableName() {
        return "Method Call Graph";
    }

    @NotNull
    @Override
    public DiagramExtras<ReferenceElement> getExtras() {
        return myExtras;
    }

    @Override
    public ReferenceDiagramDataModel createDataModel(@NotNull Project project, @Nullable ReferenceElement referenceElement, @Nullable VirtualFile virtualFile, DiagramPresentationModel model) {
        PreConditionUtil.assertTrue(referenceElement.getPsiElement() instanceof PsiClass, "referenceElement.psiElement must be a " +
                "PsiClass");
        return new ReferenceDiagramDataModel(project, referenceElement);
    }

    @Override
    public DiagramColorManager getColorManager() {
        return myColorManager;
    }

    public static ReferenceDiagramProvider getInstance() {
        return (ReferenceDiagramProvider) DiagramProvider.findByID(ID);
    }

}