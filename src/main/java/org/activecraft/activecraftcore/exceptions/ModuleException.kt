package org.activecraft.activecraftcore.exceptions

class ModuleException(val moduleName: String, val errorType: ErrorType, message: String? = errorType.message) :
    ActiveCraftException(message) {

    enum class ErrorType(val message: String) {
        DOES_NOT_EXIST("Module does not exist"),
        NOT_INSTALLED("Module not installed"),
        ALREADY_INSTALLED("Module already installed"),
        NOT_LOADED("Module not loaded"),
        ALREADY_LOADED("Module already loaded"),
        NOT_ENABLED("Module not enabled"),
        ALREADY_ENABLED("Module already enabled")
    }
}