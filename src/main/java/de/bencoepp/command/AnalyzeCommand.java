package de.bencoepp.command;

import de.bencoepp.utils.DirectoryHelper;
import me.tongfei.progressbar.ProgressBar;
import org.barfuin.texttree.api.DefaultNode;
import org.barfuin.texttree.api.TextTree;
import org.barfuin.texttree.api.TreeOptions;
import org.barfuin.texttree.api.style.TreeStyle;
import picocli.CommandLine;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "analyze",
        sortOptions = false,
        headerHeading = "@|bold,underline Usage:|@%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description:|@%n%n",
        parameterListHeading = "%n@|bold,underline Parameters:|@%n",
        optionListHeading = "%n@|bold,underline Options:|@%n",
        header = "analyze any container project you have",
        description = "analyze the project by executing the command please choose a visualization method")
public class AnalyzeCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-t", "--tree"},
            description = "visualize project as tree")
    boolean tree;

    @CommandLine.Option(names = {"-s", "--scan"},
            description = "scan docker and docker compose files")
    boolean scan;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public Integer call() throws Exception {
        boolean ok = true;
        String currentDir = System.getProperty("user.dir");
        List<File> listFiles = new ArrayList<>();
        List<File> listDockerfiles = new ArrayList<>();
        List<File> listDockerComposeFiles = new ArrayList<>();
        try (ProgressBar pb = new ProgressBar("Analyzing", 2)) {
            pb.setExtraMessage("Get Path...");
            pb.step();
            pb.setExtraMessage("Gathering files...");
            listFiles = DirectoryHelper.getFilesFromDirectory(currentDir);
            pb.step();
            pb.setExtraMessage("Separating Files...");
            for (File file : listFiles) {
                if(file.getName().equals("Dockerfile")){
                    listDockerfiles.add(file);
                }
                if(file.getName().equals("docker-compose.yaml")){
                    listDockerComposeFiles.add(file);
                }
                if(file.getName().equals("docker-compose.yml")){
                    listDockerComposeFiles.add(file);
                }
            }
            pb.setExtraMessage("Finished...");
        }
        String project = currentDir.substring(currentDir.lastIndexOf("\\") + 1 ,currentDir.length());
        if(tree && !scan){
            DefaultNode tree = new DefaultNode(project);
            for (ListIterator<File> iter = listFiles.listIterator(); iter.hasNext(); ) {
                File element = iter.next();
                String filePath = element.getCanonicalPath().replace(currentDir + "\\", "");
                if(!filePath.contains("\\")){
                    DefaultNode node = new DefaultNode(element.getName());
                    if(element.getName().equals("Dockerfile")){
                        node.setAnnotation("(This file will be analyzed further)");
                    }
                    if(element.getName().equals("docker-compose.yaml")){
                        node.setAnnotation("(This file will be analyzed further)");
                    }
                    if(element.getName().equals("docker-compose.yml")){
                        node.setAnnotation("(This file will be analyzed further)");
                    }
                    tree.addChild(node);
                    iter.remove();
                }
            }
            TreeOptions options = new TreeOptions();
            options.setStyle(new TreeStyle("├─── ", "│    ", "╰─── ", "<", ">"));
            String rendered = TextTree.newInstance(options).render(tree);
            System.out.println(rendered);
        }
        if(scan && !tree){
            int count = listDockerfiles.size() + listDockerComposeFiles.size() + 3;
            try (ProgressBar pb = new ProgressBar("Analyzing", count)) {
                pb.setExtraMessage("Scanning files...");
                pb.step();
                for (File dockerfile : listDockerfiles) {
                    pb.step();
                }
                pb.step();
                for (File dockerComposeFile : listDockerComposeFiles) {
                    pb.step();
                }
                pb.step();
            }
        }
        if(!tree && !scan){
            spec.commandLine().usage(System.err);
        }
        return ok ? 0 : 1;
    }
}
