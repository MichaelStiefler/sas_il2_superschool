/*
  FlickrDownload - Copyright(C) 2010-2011 Brian Masney <masneyb@onstation.org>.
  If you have any questions, comments, or suggestions about this program, please
  feel free to email them to me. You can always find out the latest news about
  FlickrDownload from my website at http://www.onstation.org/flickrdownload/

  FlickrDownload comes with ABSOLUTELY NO WARRANTY; for details, see the COPYING
  file. This is free software, and you are welcome to redistribute it under
  certain conditions; for details, see the COPYING file.
*/

package org.gftp.FlickrDownload;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.gftp.FlickrDownload.Stats.MediaStats;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Exif;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.Note;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.tags.Tag;

public abstract class AbstractSet {
	public static String SET_XML_FILENAME = "photos.xml";
	public static String SMALL_SQUARE_PHOTO_DESCRIPTION = "Small Square";
    public static String LARGE_SQUARE_PHOTO_DESCRIPTION = "Large Square";
	public static String THUMBNAIL_PHOTO_DESCRIPTION = "Thumbnail";
	public static String SMALL_PHOTO_DESCRIPTION = "Small";
    public static String SMALL_320_PHOTO_DESCRIPTION = "Small 320";
	public static String MEDIUM_PHOTO_DESCRIPTION = "Medium";
    public static String MEDIUM_640_PHOTO_DESCRIPTION = "Medium 640";
    public static String MEDIUM_800_PHOTO_DESCRIPTION = "Medium 800";
	public static String LARGE_PHOTO_DESCRIPTION = "Large";
    public static String LARGE_1600_PHOTO_DESCRIPTION = "Large 1600";
    public static String LARGE_2048_PHOTO_DESCRIPTION = "Large 2048";
	public static String ORIGINAL_MEDIA_DESCRIPTION = "Original";

	private Configuration configuration;
	private Collection<String> expectedFiles = new HashSet<String>(Arrays.asList(
			SET_XML_FILENAME, 
			Sets.SET_THUMBNAIL_FILENAME,
			Sets.SET_DETAIL_FILENAME));
	
	public AbstractSet (Configuration configuration) {
		this.configuration = configuration;
	}

	protected abstract int getMediaCount();
	protected abstract String getSetId();
	protected abstract String getSetTitle();
	protected abstract String getSetDescription();
	protected abstract String getPrimaryPhotoId();
	protected abstract String getPrimaryPhotoSmallSquareUrl();
	protected abstract void download(Flickr flickr, Element setXml) throws IOException, SAXException, FlickrException;

	public File getSetDirectory() {
		return new File(this.configuration.photosBaseDirectory, getSetId());
	}

	public File getSetXmlFilename() {
		return new File(getSetDirectory(), SET_XML_FILENAME);
	}

	private Element createStatsXml() throws JDOMException, IOException {
		Map<String, MediaStats> allStats = new HashMap<String, MediaStats>();
		Stats.processXmlFile(getSetXmlFilename(), allStats, getSetId(), NoopMediaIndexer.INSTANCE);
		return Stats.generateStatsXml(allStats);
	}

	public Element createToplevelXml() throws JDOMException, IOException {
		String setThumbnailBaseFilename = String.format("%s_thumb_sq.jpg", getPrimaryPhotoId());
		File setDir = new File(this.configuration.photosBaseDirectory, getSetId());
		return new Element("set")
				.addContent(new Element("id").setText(getSetId()))
				.addContent(new Element("title").setText(getSetTitle()))
				.addContent(new Element("description").setText(getSetDescription()))
				.addContent(XmlUtils.downloadMediaAndCreateElement("thumbnailFile", 
						new File(setDir, setThumbnailBaseFilename), 
						getSetId() + File.separator + setThumbnailBaseFilename, 
						getPrimaryPhotoSmallSquareUrl(), false, this.configuration))
				.addContent(createStatsXml());
	}

	protected void processPhoto(Photo photo, Flickr flickr, Element setXml) throws IOException, SAXException, FlickrException {
            // We probably have some of the photo data from a search
            // result, but probably not all, so fetch it all.
	    
	        int repeat = 10;
	        while (true) {
	            try {
    	            photo = flickr.getPhotosInterface().getPhoto(photo.getId());
    	            break;
    	        } catch (FlickrException e) {
                    if (repeat-- == 0)
                        throw e;
    	            try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        // TODO Auto-generated catch block
                        ie.printStackTrace();
                    }
    	        }
	        }

            Element tagEle = new Element("tags");
            for (Object tagObj : photo.getTags()) {
            	Tag tag = (Tag) tagObj;
            	tagEle.addContent(new Element("tag")
            		.setAttribute("author", tag.getAuthor())
            		.setAttribute("value", tag.getValue())
                    .setAttribute("raw", tag.getRaw()));
            }

            Element notesEle = new Element("notes");
            for (Note note : photo.getNotes()) {
            	notesEle.addContent(new Element("note")
            		.setAttribute("id", note.getId())
            		.setAttribute("author", note.getAuthor())
            		.setAttribute("text", note.getText())
            		.setAttribute("x", String.valueOf(note.getBounds().getX()))
            		.setAttribute("y", String.valueOf(note.getBounds().getY()))
            		.setAttribute("width", String.valueOf(note.getBounds().getWidth()))
            		.setAttribute("height", String.valueOf(note.getBounds().getHeight())));
            }

            Element exifTagsEle = new Element("exif");
            if (this.configuration.downloadExifData) {
            	for (Exif exif : flickr.getPhotosInterface().getExif(photo.getId(), photo.getSecret())) {
            		exifTagsEle.addContent(new Element("exif")
            			.setAttribute("clean", StringUtils.defaultString(exif.getClean()))
            			.setAttribute("label", StringUtils.defaultString(exif.getLabel()))
            			.setAttribute("raw", StringUtils.defaultString(exif.getRaw()))
            			.setAttribute("tag", StringUtils.defaultString(exif.getTag()))
            			.setAttribute("tagspace", StringUtils.defaultString(exif.getTagspace()))
            			.setAttribute("tagspaceId", StringUtils.defaultString(exif.getTagspaceId())));
            	}
            }

            String originalUrl = null;
            String originalBaseFilename;
            if (photo.getMedia().equals("video")) {
            	originalUrl = getOriginalVideoUrl(flickr, photo.getId());
            	originalBaseFilename = String.format("%s_orig.%s", photo.getId(), IOUtils.getVideoExtension(originalUrl));
            }
            else {
            	try {
            		originalUrl = photo.getOriginalUrl();
            	}
            	catch (FlickrException e) {
            		// NOOP - original URL not available
            	}
        		originalBaseFilename = String.format("%s_orig.%s", 
        				photo.getId(), 
        				photo.getOriginalFormat());
            }
            
//            System.out.println(photo.getOriginalUrl());
//            System.out.println(photo.getId());
//            System.out.println(photo.getOriginalFormat());
//            System.out.println(photo.getFarm());
//            System.out.println(photo.getPathAlias());
//            System.out.println(photo.getUrl());

            String smallSquareBaseFilename = String.format("%s_thumb_sq.jpg", photo.getId());
            String mediumBaseFilename = String.format("%s_med.jpg", photo.getId());
            String largeBaseFilename = String.format("%s_large.jpg", photo.getId());
            
            File smallSquareFile = null,
                    largeSquareFile = null,
                    thumbnailFile = null,
                    smallFile = null,
                    small320File = null,
                    mediumFile = null,
                    medium640File = null,
                    medium800File = null,
                    largeFile = null,
                    large1600File = null,
                    large2048File = null,
                    originalFile = null;
            if (this.configuration.flickrFolders) {
                smallSquareFile = new File(this.configuration.photosBaseDirectory, getFlickrFileName(photo.getSmallSquareUrl()));
                largeSquareFile = new File(this.configuration.photosBaseDirectory, getFlickrFileName(photo.getSquareLargeUrl()));
                thumbnailFile = new File(this.configuration.photosBaseDirectory, getFlickrFileName(photo.getThumbnailUrl()));
                smallFile = new File(this.configuration.photosBaseDirectory, getFlickrFileName(photo.getSmallUrl()));
                small320File = new File(this.configuration.photosBaseDirectory, getFlickrFileName(photo.getSmall320Url()));
                mediumFile = new File(this.configuration.photosBaseDirectory, getFlickrFileName(photo.getMediumUrl()));
                medium640File = new File(this.configuration.photosBaseDirectory, getFlickrFileName(photo.getMedium640Url()));
                medium800File = new File(this.configuration.photosBaseDirectory, getFlickrFileName(photo.getMedium800Url()));
                largeFile = new File(this.configuration.photosBaseDirectory, getFlickrFileName(photo.getLargeUrl()));
                large1600File = new File(this.configuration.photosBaseDirectory, getFlickrFileName(photo.getLarge1600Url()));
                large2048File = new File(this.configuration.photosBaseDirectory, getFlickrFileName(photo.getLarge2048Url()));
                originalFile = new File(this.configuration.photosBaseDirectory, getFlickrFileName(photo.getOriginalUrl()));
                
            } else {
                smallSquareFile = new File(getSetDirectory(), smallSquareBaseFilename);
                mediumFile = new File(getSetDirectory(), mediumBaseFilename);
                largeFile = new File(getSetDirectory(), largeBaseFilename);
                originalFile = new File(getSetDirectory(), originalBaseFilename);
            }
//            System.out.println(smallSquareFile.getAbsolutePath());
//            System.out.println(mediumFile.getAbsolutePath());
//            System.out.println(largeFile.getAbsolutePath());
//            System.out.println(originalFile.getAbsolutePath());
//            System.exit(1);

    		this.expectedFiles.add(String.format("%s.html", photo.getId()));
    		this.expectedFiles.add(smallSquareBaseFilename);
    		this.expectedFiles.add(mediumBaseFilename);
    		this.expectedFiles.add(largeBaseFilename);
    		this.expectedFiles.add(originalBaseFilename);

            GeoData geoData = photo.getGeoData();
            Element media = new Element("media")
            	.setAttribute("type", photo.getMedia())
            	.addContent(new Element("id").setText(photo.getId()))
            	.addContent(new Element("title").setText(photo.getTitle()))
            	.addContent(new Element("description").setText(photo.getDescription()))
    			.addContent(new Element("publicUrl").setText(photo.getUrl()));

            if(!this.configuration.onlyOriginals) {
    			media.addContent(XmlUtils.createMediaElement("image", null, null, photo.getThumbnailUrl())
    					.setAttribute("type", THUMBNAIL_PHOTO_DESCRIPTION))
    			.addContent(XmlUtils.createMediaElement("image", null, null, photo.getSmallUrl())
    					.setAttribute("type", SMALL_PHOTO_DESCRIPTION));
    			
    			if (smallSquareFile != null)
    			    media.addContent(XmlUtils.downloadMediaAndCreateElement("image",
                	        smallSquareFile, 
                	        smallSquareFile.getName(),
                			photo.getSmallSquareUrl(),
                			false,
                            this.configuration)
            				.setAttribute("type", SMALL_SQUARE_PHOTO_DESCRIPTION));
    			
                if (largeSquareFile != null)
                    media.addContent(XmlUtils.downloadMediaAndCreateElement("image",
                            largeSquareFile, 
                            largeSquareFile.getName(),
                            photo.getSquareLargeUrl(),
                            false,
                            this.configuration)
                                .setAttribute("type", LARGE_SQUARE_PHOTO_DESCRIPTION));
    			
                if (thumbnailFile != null)
                    media.addContent(XmlUtils.downloadMediaAndCreateElement("image",
                            thumbnailFile, 
                            thumbnailFile.getName(),
                            photo.getThumbnailUrl(),
                            false,
                            this.configuration)
                                .setAttribute("type", THUMBNAIL_PHOTO_DESCRIPTION));

                if (smallFile != null)
                    media.addContent(XmlUtils.downloadMediaAndCreateElement("image",
                            smallFile, 
                            smallFile.getName(),
                            photo.getSmallUrl(),
                            false,
                            this.configuration)
                                .setAttribute("type", SMALL_PHOTO_DESCRIPTION));
                
                if (small320File != null)
                    media.addContent(XmlUtils.downloadMediaAndCreateElement("image",
                            small320File, 
                            small320File.getName(),
                            photo.getSmall320Url(),
                            false,
                            this.configuration)
                                .setAttribute("type", SMALL_320_PHOTO_DESCRIPTION));
                
                if (mediumFile != null)
                    media.addContent(XmlUtils.downloadMediaAndCreateElement("image",
                            mediumFile, 
                            mediumFile.getName(),
                            photo.getMediumUrl(),
                            false,
                            this.configuration)
                                .setAttribute("type", MEDIUM_PHOTO_DESCRIPTION));
                
                if (medium640File != null)
                    media.addContent(XmlUtils.downloadMediaAndCreateElement("image",
                            medium640File, 
                            medium640File.getName(),
                            photo.getMedium640Url(),
                            false,
                            this.configuration)
                                .setAttribute("type", MEDIUM_640_PHOTO_DESCRIPTION));
                
                if (medium800File != null)
                    media.addContent(XmlUtils.downloadMediaAndCreateElement("image",
                            medium800File, 
                            medium800File.getName(),
                            photo.getMedium800Url(),
                            false,
                            this.configuration)
                                .setAttribute("type", MEDIUM_800_PHOTO_DESCRIPTION));
                
                if (largeFile != null)
                    media.addContent(XmlUtils.downloadMediaAndCreateElement("image",
                            largeFile, 
                            largeFile.getName(),
                            photo.getLargeUrl(),
                            false,
                            this.configuration)
                                .setAttribute("type", LARGE_PHOTO_DESCRIPTION));
                
                if (large1600File != null)
                    media.addContent(XmlUtils.downloadMediaAndCreateElement("image",
                            large1600File, 
                            large1600File.getName(),
                            photo.getLarge1600Url(),
                            false,
                            this.configuration)
                                .setAttribute("type", LARGE_1600_PHOTO_DESCRIPTION));
                
                if (large2048File != null)
                    media.addContent(XmlUtils.downloadMediaAndCreateElement("image",
                            large2048File, 
                            large2048File.getName(),
                            photo.getLarge2048Url(),
                            false,
                            this.configuration)
                                .setAttribute("type", LARGE_2048_PHOTO_DESCRIPTION));
                
            }

            media.addContent(XmlUtils.downloadMediaAndCreateElement("image",
                    originalFile, 
                    originalFile.getName(),
                    originalUrl,
                    false,
                    this.configuration)
                        .setAttribute("type", ORIGINAL_MEDIA_DESCRIPTION)
                        .setAttribute("format", photo.getOriginalFormat()))
           		.addContent(new Element("dates")               		
           			.addContent(XmlUtils.createDateElement("taken", photo.getDateTaken())
           				.setAttribute("granularity", photo.getTakenGranularity()))
           			.addContent(XmlUtils.createDateElement("uploaded", photo.getDatePosted()))
           			.addContent(XmlUtils.createDateElement("lastUpdate", photo.getLastUpdate())))
            	.addContent(new Element("license").setText(Licenses.getLicense(flickr, photo.getLicense())))
            	.addContent(new Element("primary").setText(photo.getId().equals(getPrimaryPhotoId()) ? "1" : "0"))
            	.addContent(new Element("privacy")
            		.setAttribute("family", (photo.isFamilyFlag() ? "1" : "0"))
            		.setAttribute("friends", (photo.isFriendFlag() ? "1" : "0"))
            		.setAttribute("public", (photo.isPublicFlag() ? "1" : "0")))
            	.addContent(new Element("rotation").setText(String.valueOf(photo.getRotation())))
            	.addContent(new Element("geodata")
            		.setAttribute("placeId", photo.getPlaceId())
            		.setAttribute("accuracy", geoData == null ? "" : String.valueOf(geoData.getAccuracy()))
            		.setAttribute("latitude", geoData == null ? "" : String.valueOf(geoData.getLatitude()))
            		.setAttribute("longitude", geoData == null ? "" : String.valueOf(geoData.getLongitude())))
              	.addContent(tagEle)
               	.addContent(notesEle)
               	.addContent(exifTagsEle);

            setXml.addContent(media);
	}

	public Element createSetlevelXml(Flickr flickr) throws IOException, SAXException, FlickrException {
		Logger.getLogger(getClass()).info(String.format("Downloading information for set %s - %s",
				getSetId(), getSetTitle()));

		Element setXml = new Element("set")
				.addContent(XmlUtils.createApplicationXml())
				.addContent(XmlUtils.createUserXml(this.configuration))
				.addContent(new Element("id").setText(getSetId()))
				.addContent(new Element("title").setText(getSetTitle()))
				.addContent(new Element("description").setText(getSetDescription()));

		download(flickr, setXml);

		IOUtils.findFilesThatDoNotBelong(getSetDirectory(), this.expectedFiles, this.configuration.addExtensionToUnknownFiles);
		return setXml;
	}

	private static String getOriginalVideoUrl(Flickr flickr, String photoId) throws IOException, FlickrException, SAXException {
		String origUrl = null;
		String hdUrl = null;
		String siteUrl = null;
		for (Size size : flickr.getPhotosInterface().getSizes(photoId, true)) {
			if (size.getSource().contains("/play/orig"))
				origUrl = size.getSource();
			else if (size.getSource().contains("/play/hd"))
				hdUrl = size.getSource();
			else if (size.getSource().contains("/play/site"))
				siteUrl = size.getSource();
		}
		if (origUrl != null)
			return origUrl;
		else if (hdUrl != null)
			return hdUrl;
		else if (siteUrl != null)
			return siteUrl;
		else
			return null;
	}
	
	public static String getFlickrFileName(String originalUrl) {
	    return originalUrl.substring(ordinalIndexOf(originalUrl, "/", 3) + 1);
	}
	
	public static int ordinalIndexOf(String str, String substr, int n) {
	    int pos = str.indexOf(substr);
	    while (--n > 0 && pos != -1)
	        pos = str.indexOf(substr, pos + 1);
	    return pos;
	}
}
