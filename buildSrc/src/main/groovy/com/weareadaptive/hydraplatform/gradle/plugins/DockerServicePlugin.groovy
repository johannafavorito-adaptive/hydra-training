package com.weareadaptive.hydraplatform.gradle.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.*
import org.gradle.process.ExecSpec

import java.nio.file.Path

@FunctionalInterface
interface DockerComposeAction {
    List<String> args();
}

class DockerServiceTask extends DefaultTask {
    @Input
    String executable = 'docker'

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    Path projectDir = project.rootDir.toPath().resolve('docker')

    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    Path dockerComposeFile = projectDir.resolve("docker-compose.yml")

    @Input
    DockerComposeAction action

    @TaskAction
    void doIt() {
        List<String> fullArgList = [
                'compose', '--ansi', 'never',
                '--project-directory', projectDir.toString(),
                '--file', dockerComposeFile.toString()
        ]

        fullArgList.addAll(action.args())

        project.exec { ExecSpec execSpec ->
            execSpec.with {
                environment = ["CLUSTER_ADDRESSES": project.rootProject.ext.docker_cluster_addresses]
                executable = this.executable
                args = fullArgList
            }
        }
    }
}

class DockerServicePlugin implements Plugin<Project> {
    @Override
    void apply(final Project project) {

        project.tasks.register('startServices', DockerServiceTask.class, { task ->
            action = {
                def services = project.rootProject.ext.docker_services ? project.rootProject.ext.docker_services : []
                return ["up", "-d"] + services
            }
            dependsOn = [':docker:buildContainers']
        })

        project.tasks.register('stopServices', DockerServiceTask.class, { task ->
            action = {
                return ["down", "--volumes"]
            }
        })
    }
}
