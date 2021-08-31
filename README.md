# AdminSecure
_The best plugin to protect anarchy servers and mc servers in general against op attacks_

### How does it work?
When the server detects a player has been oped or has gamemode creative or special permissions it sends a message to the webhook you configured. Then, it tells the player to input the code sent to the webhook and if the code is valid, it verifies the player but if the code isn't valid, it kicks the player(or what you told the plugin to do).

### Commands
* /adminsc \<code> **->** ***Verify command.***
* /reload **->** ***Reload the configuration.***
* /adminsc **->** ***Info command.***

### Permissions
* **adminsecure.command.adminsc** *(/adminsc command permission)*
* **adminsecure.code** *(Main webhook trigger permission)*

### Config
```yaml
discord_webhook: 'https://discord.com/api/webhooks/YOUR/DISCORD_WEBHOOK_HERE' # Your discord webhook.
messages:
  prefix: '&8[&cAdmin&3Secure&8]&r ' # Message prefix.
  auth_wait: '&6Please wait...' # Connecting message.
  auth_error: '&cThere was an error trying to communicate to the webhook! Please rejoin or check config!' # Error message.
  auth_request: "&6We need to verify it's you, please look at your administration discord and put the following command &e/adminsc <code>" # Code request messsage.
  auth_valid: "&aWe have successfully verified it's you! &6&nHave fun!" # Valid code message.
  auth_invalid: "&cThe code was invalid!" # Invalid code message.
login_persistence: # Send a message every X ms.
  enabled: true
  delay: 5000
commands:
  auth_valid: # Commands sent when valid code.
    - ''
  auth_invalid: # Commands sent when invalid code.
    - 'kick %player_name% &cThe code was invalid!'
security_code: # Security code config.
  max_number: 9999999 # Max code number.
  allowed_commands: # Allowed commands while verification process.
    - login
    - l
    - reg
    - register
  join_config: # Config when joining.
    apply_blindness: true # Apply blindness to player when joining.
    play_sound: true # Play a sound when joining.
    triggers:
      gamemode: true # Trigger webhook when gamemode.
      op: true # Trigger webhook when op.
      permissions: # Trigger webhook when permissions. (Apart from the main one)
        enabled: true 
        perms: # Permissions that trigger the plugin
          - worldedit.*
          - minecraft.command.teleport
          - minecraft.command.tp
          - minecraft.command.gamemode
          - minecraft.command.op
  responses: # Webhook messages.
    auth_request:
      content: # Content of webhook message.
        0:
          type: EMBED
          content: # Content of embed.
            0:
              type: TITLE
              content: AdminSecure
            1:
              type: DESCRIPTION
              content: 'Security Code requested!'
            2:
              type: FIELD
              content:
                title: Player
                content: '%player_name%'
                inline: true
            3:
              type: FIELD
              content:
                title: Code
                content: '%new_code%'
                inline: true
            4:
              type: COLOR
              content: YELLOW
            5:
              type: AUTHOR
              content:
                author: AdminSecure
                url: https://github.com/PK2-Stimpy
                imageUrl: https://i.imgur.com/LR2A6vq.png
            6:
              type: FOOTER
              content:
                footer: PK2_Stimpy
                url: https://i.imgur.com/LR2A6vq.png
    auth_valid:
      content:
        0:
          type: EMBED
          content:
            0:
              type: TITLE
              content: AdminSecure
            1:
              type: DESCRIPTION
              content: 'Security Code was correct!'
            2:
              type: FIELD
              content:
                title: Player
                content: '%player_name%'
                inline: true
            3:
              type: COLOR
              content: GREEN
            4:
              type: AUTHOR
              content:
                author: AdminSecure
                url: https://github.com/PK2-Stimpy
                imageUrl: https://i.imgur.com/LR2A6vq.png
            5:
              type: FOOTER
              content:
                footer: PK2_Stimpy
                url: https://i.imgur.com/LR2A6vq.png
    auth_invalid:
      content:
        0:
          type: EMBED
          content:
            0:
              type: TITLE
              content: AdminSecure
            1:
              type: DESCRIPTION
              content: 'Security Code was incorrect!'
            2:
              type: FIELD
              content:
                title: Player
                content: '%player_name%'
                inline: true
            3:
              type: COLOR
              content: RED
            4:
              type: AUTHOR
              content:
                author: AdminSecure
                url: https://github.com/PK2-Stimpy
                imageUrl: https://i.imgur.com/LR2A6vq.png
            5:
              type: FOOTER
              content:
                footer: PK2_Stimpy
                url: https://i.imgur.com/LR2A6vq.png
```

##### Webhook messages content
###### Types
* **AVATAR_URL**
    * **content** = Url.
* **CONTENT**
    * **content** = Message content.
* **EMBED**
    * **content** = Embed object list.
* **TTS**
    * **content** = true/false.
* **USERNAME**
    * **content** = Webhook username.
###### Embed object types
* **AUTHOR**
    * **content**
        * **author** = Author name.
        * **url** = Author website url.
        * **imageUrl** = Image url. (Ex. https://i.imgur.com/rO7A5O5.jpeg)
* **COLOR**
    * **content** = Embed color. (Ex. RED, BLUE, BLACK, PINK)
* **DESCRIPTION**
    * **content** = Description.
* **FIELD**
    * **content**
        * **title** = Title of field.
        * **content** = Content of field.
        * **inline** = true/false. (Field goes inline)
* **FOOTER**
    * **content**
        * **footer** = Footer message.
        * **url** = Footer image url.
* **IMAGE**
    * **content** = Image url.
* **THUMBNAIL**
    * **content** = Thumbnail image url.
* **TITLE**
    * **content** = Embed title.
* **URL**
    * **content** = Embed url.