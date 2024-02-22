package gatling;

import io.gatling.recorder.GatlingRecorder;
import io.gatling.recorder.config.RecorderPropertiesBuilder;
import io.gatling.recorder.render.template.Format;
import scala.Option;

public class Recorder {
    public static void main(String[] args) {
        RecorderPropertiesBuilder props = new RecorderPropertiesBuilder()
                .simulationsFolder(IDEPathHelper.mavenSourcesDirectory.toString())
                .resourcesFolder(IDEPathHelper.mavenResourcesDirectory.toString())
                .simulationPackage("simulation")
                .simulationFormat(Format.fromString("java"));

        GatlingRecorder.fromMap(props.build(), Option.apply(IDEPathHelper.recorderConfigFile));
    }
}