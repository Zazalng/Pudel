package mimikko.zazalng.pudel.contracts;

public enum InteractionType {
    TEXT,           // From message command
    REACTION,       // From emoji reaction
    SLASH,          // Slash command
    AUTOCOMPLETE,   // Autocomplete interaction
    BUTTON,         // Button press
    MODAL,          // Modal submission
    MENU_CONTEXT,   // Context menu right-click
    MENU_SELECTED   // Selection menu
}