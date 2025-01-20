package com.weareadaptive.hydraplatform.gradle

class HydraContainerUtils {
    static String imageName(String name, def project) {
        def hydraVersion = project.rootProject.ext.hydraVersion
        if (isHashBuild(hydraVersion)) {
            // local images need to be prefixed with docker://
            return "docker://weareadaptive-hydra-docker-snapshot.jfrog.io/${name}-snapshot:latest"
        } else if (isSnapshotBuild(hydraVersion)) {
            // build Hydra with ./docker/docker-build-local-release.sh
            return "docker://weareadaptive-hydra-docker-release.jfrog.io/${name}-release:${hydraVersion}"
        } else {
            return "weareadaptive-hydra-docker-release.jfrog.io/${name}-release:${hydraVersion}"
        }
    }

    private static boolean isHashBuild(String hydraVersion) {
        return hydraVersion =~ /-[a-f0-9]{4,}$/
    }

    private static boolean isSnapshotBuild(String hydraVersion) {
        return hydraVersion =~ /-SNAPSHOT$/
    }
}
