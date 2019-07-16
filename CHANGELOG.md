# Changelog
## [3.11.6](https://github.com/xamoom/xamoom-ios-sdk/compare/3.11.4...3.11.5) - 16.07.2019
- set correct ContentReason when loading Beacons in XamoomBeaconService  

## [3.11.5](https://github.com/xamoom/xamoom-ios-sdk/compare/3.11.4...3.11.5) - 24.06.2019
- Add function for setting ContentBlock9ViewHolder navigation type 

## [3.11.4](https://github.com/xamoom/xamoom-ios-sdk/compare/3.11.3...3.11.4) - 13.05.2019
- Improvments for content password challange
- Show x-forbidden after three times wrong password entry
- UI improvements for ContentBlock2 (Video Block)

- Fix crash for ContentBlock9Adapter if reopen a content with a spot map
- Fix issue for Vimeo videos, which were not able to play (opened an intent)

## [3.11.3](https://github.com/xamoom/xamoom-ios-sdk/compare/3.11.1...3.11.3) - 06.05.2019
- Add function for "x-forbidden" handling
- Add callback for content password protection

- Improvements for Beacon ranging in XamoomBeaconService

## [3.11.1](https://github.com/xamoom/xamoom-ios-sdk/compare/3.10.8...3.10.9-beta1) - 18.04.2019
- Fix crash when cancel XamoomContentFragment loading

## [3.11.0](https://github.com/xamoom/xamoom-ios-sdk/compare/3.10.8...3.10.9-beta1) - 25.03.2019
- Add Mapbox
- Add Beacon logic
- Add FCM logic 
- Add Location change logic 

## [3.10.9-beta1](https://github.com/xamoom/xamoom-ios-sdk/compare/3.10.8...3.10.9-beta1) - 04.03.2019
- Mapbox

## [3.10.8](https://github.com/xamoom/xamoom-ios-sdk/compare/3.10.6...3.10.7) - 25.02.2019
- UI improvements

## [3.10.7](https://github.com/xamoom/xamoom-ios-sdk/compare/3.10.6...3.10.7) - 28.01.2019
- Added web view for open links in app (with given url format)
- Added copyright label for coverimage
- Added new endpoint urls

- Improved image title font size

## [3.10.6](https://github.com/xamoom/xamoom-ios-sdk/compare/3.10.5...3.10.6) - 25.09.2018
- Correct Production Endpoint for Xamoom 3

## [3.10.5](https://github.com/xamoom/xamoom-ios-sdk/compare/3.10.4...3.10.5) - 24.09.2018
- Production Endpoint for Xamoom 3

## [3.10.4](https://github.com/xamoom/xamoom-ios-sdk/compare/3.10.3-beta1...3.10.4) - 21.09.2018
- Production Endpoint for Xamoom 3

## [3.10.2](https://github.com/xamoom/xamoom-ios-sdk/compare/3.10.2...3.10.3-beta1) - 12.09.2018
- Xamoom 3 backend

## [3.10.3](https://github.com/xamoom/xamoom-ios-sdk/compare/3.10.2...3.10.3) - 16.07.2018

- Fixed spotmap crash when no spot is shown
- Fixed issue where lists do not order descending.
- Fixed scrolling issue when content only contains a list.

## [3.10.2](https://github.com/xamoom/xamoom-ios-sdk/compare/3.10.1...3.10.2) - 12.06.2018

- Added missing ephemeral header to calls
- Added recommendation call

- Fixed wrongly used obtainStyledAttributes
- Fixed crash when cleaning ContenBlockViewHolders
- Fixed textColoring only when set in style
- Fixed usage of CustomContentBlocks in lists

- Updated to android sdk 26

## [3.10.1](https://github.com/xamoom/xamoom-ios-sdk/compare/3.10.0...3.10.1) - 15.02.2018

- Fixed audioPlayer to not stop when user cancels notification
- Fixed problem when clicking forward/backward button before connecting to service

## [3.10.0](https://github.com/xamoom/xamoom-ios-sdk/compare/3.9.0...3.10.0) - 13.02.2018

- Added rtl support to ContentBlocks

## [3.9.0](https://github.com/xamoom/xamoom-ios-sdk/compare/3.8.0...3.9.0) - 05.02.2018

- Added ephemeralId
- Added reasons to content calls

- Changed audioplayerblock to use service & show notification

## [3.8.0](https://github.com/xamoom/xamoom-ios-sdk/compare/3.7.3...3.8.0) - 06.12.2017

- Added ContentBlocksTheme for styling contentBlocks

- Fixed textView margins on ContentBlock11ViewHolder
- Fixed textView margins on ContentBlock0ViewHolder

- Removed placeholder on Contentblock11ViewHolder

- Updated HtmlTextView dependency

## [3.7.3](https://github.com/xamoom/xamoom-ios-sdk/compare/3.7.2...3.7.3) - 16.11.2017

- Fixed contentListTags parsing
- Added contentList localization

## [3.7.2](https://github.com/xamoom/xamoom-android-sdk/compare/3.7.1...3.7.2) - 06.11.2017

- Fixed normalizing of appname in User-Agent

## [3.7.1](https://github.com/xamoom/xamoom-android-sdk/compare/3.7.0...3.7.1) - 06.11.2017

- Fixed normalizing of appname in User-Agent

## [3.7.0](https://github.com/xamoom/xamoom-android-sdk/compare/3.6.0...3.7.0) - 30.10.2017

- Added socialSharing url to content
- Added eventPackage

- Fixed phone links

## [3.6.0](https://github.com/xamoom/xamoom-android-sdk/compare/3.5.3...3.6.0) - 13.10.2017

- Added new content block: content list

## [3.5.3](https://github.com/xamoom/xamoom-android-sdk/compare/v3.5.2..v3.5.3)

* Added x-datetime condition to all getByLocationIdentifier calls

* Renamed FileProvider to XamoomFileProvider

* Fixed cut off text in ContentBlock6

## [3.5.2](https://github.com/xamoom/xamoom-android-sdk/compare/v3.5.1..v3.5.2)

* Fixed issue with global glide use in ContentBlocks

* Updated htmltextview

## [3.5.1](https://github.com/xamoom/xamoom-android-sdk/compare/v3.5.0...v3.5.1)

* Added normalizing of User-Agent

## [3.5.0](https://github.com/xamoom/xamoom-android-sdk/compare/v3.4.0...v3.5.0)

- Added support for youtube timestamps
- Added HtmlTextView to replace Webviews for formatted text

- Fix wrong displaying of audio players moving bars on lower density phones

## [3.4.0](https://github.com/xamoom/xamoom-android-sdk/compare/v3.3.1...v3.4.0)

- Added conditional content calls
- Added missing content descriptions

- Raised targetSDK & support libraries to 25

- Fixed (quick) not loading webviews

## [3.3.1](https://github.com/xamoom/xamoom-android-sdk/compare/v3.3.0...v3.3.1)

- Fixed push notificaion handler receiver

## [3.3.0](https://github.com/xamoom/xamoom-android-sdk/compare/v3.2.5...v3.3.0)

- Added push service
- Changed to MIT license

## [3.2.5](https://github.com/xamoom/xamoom-android-sdk/compare/v3.2.4...v3.2.5)

- Fixed excerpt size getting bigger on every reuse

## [3.2.4](https://github.com/xamoom/xamoom-android-sdk/compare/v3.2.3...v3.2.4)

- Added error handling on Media Player respoding to MEDIA_ERROR_SYSTEM

- Fixed wrong seeking on Media Player


## [3.2.3](https://github.com/xamoom/xamoom-android-sdk/compare/v3.2.2...v3.2.3)

- Added italian translations

- Changed design of multiple contentBlockViewHolders

## [3.2.2](https://github.com/xamoom/xamoom-android-sdk/compare/v3.2.1...v3.2.2)

- Fixed showing of progressview in ContentBlock6ViewHolder

- Added call as return value to all api calls

## [3.2.1](https://github.com/xamoom/xamoom-android-sdk/compare/v3.2.0...v3.2.1)

- Added call as return value to all api calls

## [3.2.0](https://github.com/xamoom/xamoom-android-sdk/compare/v3.1.0...v3.2.0)

- Changed image title position to display below image (caption).
- Changed audio player. New design, forward backward seeking und playing indicator.

## [3.1.0](https://github.com/xamoom/xamoom-android-sdk/compare/v3.0.0...v3.1.0)

- Added copyright to ContentBlock
- Added copyrightTextView to ContentBlock3ViewHolder

- Fixed offlineTags in `OfflineStorageTagModule` were not saved. These are now saved to SharedPreferences.

## [3.0.0](https://github.com/xamoom/xamoom-android-sdk/compare/v2.2.3...v3.0.0)

- Added offline saving
- Added instagram linkBlock
- Added customMeta

- Fixed youtube now stops playing when fragment pauses

## [2.2.3](https://github.com/xamoom/xamoom-android-sdk/compare/v2.0.0...v2.2.3)

- Added unittests
- Added searchSpotsByName
- Added rotation support to XamoomContentFragment

- Fixed soundcloud crash

## [2.1.0](https://github.com/xamoom/xamoom-android-sdk/compare/v2.0.0...v2.1.0)

- Changed XamoomContentFragment to use adapters to let users override existing ContentBlockViewholders

## [2.0.0](https://github.com/xamoom/xamoom-android-sdk/compare/v1.0.0...2.0.0)

- Added JSON-API support
- Added vimeo support

- Changed youtube webview to youtubeThumbnailView

## [1.0.0](https://github.com/xamoom/xamoom-android-sdk/releases/tag/v1.0.0) (outdated)
