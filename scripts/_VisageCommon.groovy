/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0(the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres Almiray
 */

import griffon.util.GriffonExceptionHandler

includePluginScript('lang-bridge', '_Commons')

target(name: 'compileVisageSrc', description: "", prehook: null, posthook: null) {
    depends(compileCommons)

    File visageSrc = new File("${basedir}/src/visage")
    ant.mkdir(dir: visageSrc)
    def visageSources = resolveResources("file:/${visageSrc}/**/*.visage")
    def visageArtifactSources = resolveResources("file:/${basedir}/griffon-app/**/*.visage")

    if(!visageSources && !visageArtifactSources) {
        ant.echo(message: "[visage] No Visage sources were found.")
        return
    }

    defineVisageCompilePathAndTask()

    classpathId = 'visage.compile.classpath'
    if(argsMap.compileTrace) {
        println('-'*80)
        println "[GRIFFON] compiling to ${projectMainClassesDir}"
        println "[GRIFFON] '${classpathId}' entries"
        ant.project.getReference(classpathId).list().each{println("  $it")}
        println('-'*80)
    }

    try {
        def compileVisage = { srcdir, sources ->
            if(sourcesUpToDate(srcdir, projectMainClassesDir, '.visage')) return

            // TODO: finish
        }

        if(visageSources) compileVisage(visageSrc.absolutePath, visageSources.file.absolutePath)
        def excludedPaths = ['resources', 'i18n', 'conf']
        for(dir in new File("${basedir}/griffon-app").listFiles()) {
            def sources = resolveResources("file:/${dir}/**/*.visage")
            if(!excludedPaths.contains(dir.name) && dir.isDirectory() && sources)
                compileVisage(dir.absolutePath, sources.file.absolutePath)
        }
    } catch(Exception e) {
        if(argsMap.compileTrace) {
            GriffonExceptionHandler.sanitize(e)
            e.printStackTrace(System.err)
        }
        event('StatusFinal', ["Compilation error: ${e.message}"])
        exit(1)
    }
}

target(name: 'defineVisageCompilePathAndTask', description: "", prehook: null, posthook: null) {
    ant.path(id: 'visage.compile.classpath') {
        path(refid: 'griffon.compile.classpath')
        pathElement(location: projectMainClassesDir)
        griffonSettings.buildDependencies.each { jar ->
            pathElement(location: jar.absolutePath)
        }
    }
}
