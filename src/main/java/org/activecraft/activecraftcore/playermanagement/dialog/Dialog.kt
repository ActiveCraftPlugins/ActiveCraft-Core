package org.activecraft.activecraftcore.playermanagement.dialog

data class Dialog(val onComplete: () -> Unit, val onCancel: () -> Unit, val dialogSteps: List<DialogStep>) {

    constructor(onComplete: () -> Unit, onCancel: () -> Unit, vararg dialogSteps: DialogStep) :
            this(onComplete, onCancel, dialogSteps.toList())
}