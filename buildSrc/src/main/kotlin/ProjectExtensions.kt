import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import java.io.File

fun Project.excludeFilesFromDirectory(directory: ConfigurableFileCollection, excludePatterns: Iterable<String>) {
    directory.apply {
        setFrom(
            files(files.map { file: File ->
                fileTree(file) {
                    exclude(excludePatterns)
                }
            })
        )
    }
}