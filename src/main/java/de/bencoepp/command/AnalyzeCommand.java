package de.bencoepp.command;

import de.bencoepp.utils.DirectoryHelper;
import me.tongfei.progressbar.ProgressBar;
import org.barfuin.texttree.api.DefaultNode;
import org.barfuin.texttree.api.Node;
import org.barfuin.texttree.api.TextTree;
import org.barfuin.texttree.api.TreeOptions;
import org.barfuin.texttree.api.style.TreeStyle;
import org.barfuin.texttree.api.style.TreeStyles;
import picocli.CommandLine;

import javax.swing.tree.TreeModel;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
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
        return ok ? 0 : 1;
    }
}
