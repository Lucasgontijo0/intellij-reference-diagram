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

import java.util.List;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassInitializer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.Nullable;

/**
 * @author Stefan Zeller
 */
public class PsiUtils {

    @Nullable
    public static PsiClass getClassFromHierarchy(PsiElement psiElement) {
        PsiElement parent = psiElement.getParent();
        if (parent == null) {
            return null;
        }
        if (parent instanceof PsiClass) {
            return ((PsiClass) parent);
        } else {
            return getClassFromHierarchy(parent);
        }
    }

    @Nullable
    public static PsiElement getRootPsiElement(PsiClass psiClass, PsiElement psiElement) {
        PsiElement parent = psiElement.getParent();
        if (parent == null) {
            return null;
        }
        if (parent instanceof PsiMethod) {
            if (PsiUtils.classHasMethod(psiClass, (PsiMethod) parent)) {
                return parent;
            }
        } else if (parent instanceof PsiClassInitializer) {
            return parent;
        } else if (parent instanceof PsiField) {
            return parent;
        }

        return getRootPsiElement(psiClass, parent);
    }

    private static boolean classHasMethod(PsiClass psiClass, PsiMethod other) {
        for (PsiMethod psiMethod : psiClass.getMethods()) {
            if (psiMethod.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public static PsiClass getPsiClass(String classFQN, Project project) {
        return JavaPsiFacade.getInstance(project).findClass(classFQN, GlobalSearchScope
                .projectScope(project));
    }

    public static String getName(PsiClassInitializer psiClassInitializer) {
        if (psiClassInitializer.getModifierList().hasModifierProperty("static")) {
            return "[static init]";
        } else {
            return "[init]";
        }
    }

    public static String getPresentableName(PsiElement psiElement) {
        PsiElementDispatcher<String> psiElementDispatcher = new PsiElementDispatcher<String>() {

            @Override
            public String processClass(PsiClass psiClass) {
                return psiClass.getName();
            }

            @Override
            public String processMethod(PsiMethod psiMethod) {
                List<String> parameterArray = MethodFQN.getParameterArray(psiMethod);
                String parameterRepresentation = MethodFQN.createParameterRepresentation(parameterArray);
                return psiMethod.getName() + "(" + parameterRepresentation + ")";
            }

            @Override
            public String processField(PsiField psiField) {
                return psiField.getName();
            }

            @Override
            public String processClassInitializer(PsiClassInitializer psiClassInitializer) {
                return getName(psiClassInitializer);
            }
        };

        return psiElementDispatcher.dispatch(psiElement);
    }
}
