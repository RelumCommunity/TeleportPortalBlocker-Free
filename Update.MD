> # 3.1.0:
>> ## Changes
>> - Fixed a lang issue when commands are executed
>> - Fixed the "all" case for commands, now should always work as intended
>> - Data reset code has been reworked to work better
>> - Now reload command will effectively reload chosen blocks from break and place options
>> - Data now is saved by user when they left in order to ensure almost none data loss if the server crashes
>> - Code has been made more consistent and covers better some edge cases

> # 3.0.0:
> ### REQUIREMENT: Server running with at least java14
> ### The plugin has been completely rebuilt from scratch
>> ## Added
>> - Now the db is cached at the start of the plugin and updated every x mins and when the server is stopped/restarted
>> - New Subcommand kinds
>> - New Subcommand types
>> - Now the reload works different and handles config and lang files
>> - New config option: DBSyncTime
>> ## Changes
>> - Code optimization
>> - Db save data name changed
>> - Config rebuilt
>> - lang files rebuilt
>> - Base command changed
>> - Subcommand removename changed with resetname (will just reset to 0 instead of removing the player from the db entirely)
>> - Subcommand resetdata behavior changed
>> - Subcommand resetname now require the name as first argument instead of last
>> - Now the lang file in use is cached in the plugin, so u won't see any changes until a server restart/reload or a plugin reload
>> - NOW THERE IS JUST 1 FILE! No more legacy file, it's all in one light and clean
>> - minecraft version support 1.7.2 till 26.1.x

> # 2.3.1:
>> ## Changes
>> - Fixed an issue with VersionChecker on non legacy version due to a typing error within the ref link

> # 2.3:
>> ## Added
>> - Tested till 1.20.2
>> ## Changes
>> - Changed version checker, now is hosted on our website 
>> - Fixed api for non legacy versions
>> ## Removed
>> - Removed stacktrace when the website connection is absent (there is an informative message instead)

> # 2.2:
>> ## Added
>> - Tested till 1.19.3
>> - No deactivate in {world} option
>> ## Changes
>> - cleaned static function

> # 2.1:
>> ## Added
>> - 1.16.x support
>> ## Changes
>> - Changed some base config option

> # 2.0
>> ## Added
>> - End Gateway option that check how many blocks of a type you have broke 
>> - End Gateway option that check how many blocks of a type you have place 
>> - New permissions below
>> ## Changes
>> - Fixed NewFeatures errors 
>> - BlockBroke and BlockPlace options are set false by default

> # 1.9
>> ## Added
>> - End Portal option that check how many blocks of a type you have broke 
>> - End Portal option that check how many blocks of a type you have place 
>> - New permissions below
>> ## Changes
>> - Fixed an error with endportal block trough achievement option 
>> - Fixed an error with endgateway block trough achievement option 
>> - Fixed an error with netherportal data reset permissions 
>> - Changed database commands, check the new commands in game and new permissions below 
>> - Changed netherportal database names (just rename them adding "np" before blocksbreak and blocksplace if you don't want to reset them)
>> - Changed permission portalblockerfree.updates to portalblocker.updates 
>> - UpdateChecker won't work due to spigot api changes

> # 1.8
>> ## Added
>> - 1.15.2 Tested 
>> - Nether Portal option that check how many blocks of a type you have broke 
>> - Nether Portal option that check how many blocks of a type you have place 
>> - You can now see a list of help (/portalblocker help)
>> - Config can now be reloaded (/portalblocker reload)
>> - Databases can be resetted (directly in game)
>> - Player can be removed from databases by their names (directly in game)
>> - A lot of new Permissions
>> ## Changes
>> - Fixed several bugs

> # 1.7
>> ## Added
>> - 1.15-1.15.1 compatibility 
>> - More lang customization, now you can choose where you like to have plugin prefix just adding or removing %prefix%
>> ## Changes
>> - Fixed VersionChecker won't work due a coding error made in previous versions

> # 1.6
>> ## Added
>> - Nether Portal Block Creation Option (Request number 1) https://github.com/vaincecraft/Teleport-Portal-Blocker-Free/issues/1

> # 1.5
>> ## Added
>> - Nether Portal Deactivation Option (Request number 1) https://github.com/vaincecraft/Teleport-Portal-Blocker-Free/issues/1

> # 1.4
>> ## Added
>> - An error with netherportal 1.9.* has been found (Spigot issue) Added a console information message
>> ## Changes
>> - Plugin is now lighter 
>> - Changed a config option name ("Teleports" TO "BlockThroughAchievement")
>> - Changed some codes 
>> - Fixed some unuseful controls
>> - Using the latest 1.9 build EndGateway will work properly

> # 1.3
>> ## Added
>> - 1.7.2-1.14.4 support
>> - PermanentBlock ErrorMessage
>> ## Changes
>> - 1.9 Error caused by bad spigot build (EndGateway will be disabled if server is using spigot 1.9)

> # 1.2:
>> ## Added
>> - PermanentBlock Option

> # 1.1:
>> ## Changes
>> - Critical Version Checker BugFix

https://i.ibb.co/dgD58d6/Bottle-Taker-Banner.png
https://i.ibb.co/LJfMSxF/Bowl-Taker-Banner.png
https://i.ibb.co/X7CG2yG/Head-Rewards-Banner.png
https://i.ibb.co/BZBByq9/Infinite-Anvil-Banner.png
https://i.ibb.co/WWsn4Rx/Teleport-Portal-Blocker-Free-Banner.png
https://i.ibb.co/5M7S3zJ/Teleport-Portal-Blocker-Premium-Banner.png