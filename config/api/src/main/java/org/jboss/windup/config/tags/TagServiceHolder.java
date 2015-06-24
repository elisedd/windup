package org.jboss.windup.config.tags;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.addons.Addon;
import org.jboss.windup.util.furnace.FileExtensionFilter;
import org.jboss.windup.util.furnace.FurnaceClasspathScanner;


/**
 * Loads the tags relations from tags definition files
 * and provides API to query these relations.
 *
 * @author Ondrej Zizka, ozizka at redhat.com
 */
@Singleton
public class TagServiceHolder
{
    private static final Logger log = Logger.getLogger(TagServiceHolder.class.getName() );


    private TagService tagService = new TagService();


    @Inject
    private Furnace furnace;

    @Inject
    private FurnaceClasspathScanner scanner;


    /**
     * Loads the tag definitions from the files with a ".tags.xml" suffix from the addons.
     */
    public void loadTagDefinitions()
    {
        Map<Addon, List<URL>> addonToResourcesMap = scanner.scanForAddonMap(new FileExtensionFilter("tags.xml"));
        for (Map.Entry<Addon, List<URL>> entry : addonToResourcesMap.entrySet())
        {
            for (URL resource : entry.getValue())
            {
                try(InputStream is = resource.openStream())
                {
                    tagService.readTags(is);
                }
                catch( IOException ex )
                {
                    log.warning("Couldn't read tags definition: " + resource.toString() + " from addon " + entry.getKey().getId());
                }
            }
        }
    }


    public TagService getTagService()
    {
        return tagService;
    }


}
