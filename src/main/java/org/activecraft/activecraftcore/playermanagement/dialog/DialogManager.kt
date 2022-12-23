package org.activecraft.activecraftcore.playermanagement.dialog

import org.activecraft.activecraftcore.playermanagement.Profile

class DialogManager(val profile: Profile) {

    var activeDialogScope: DialogScope? = null
        private set
    val dialogActive: Boolean
        get() = activeDialogScope != null

    @Throws(IllegalStateException::class)
    fun openDialog(dialog: Dialog): DialogScope {
        if (dialogActive)
            throw IllegalStateException("Target already in dialog.")
        val newDialogScope = DialogScope(this, dialog).also { it.next() }
        activeDialogScope = newDialogScope
        return newDialogScope
    }

    fun closeDialog() {
        if (dialogActive)
            throw IllegalStateException("Target not in dialog.")
        val dialogOfActiveScope = activeDialogScope!!.dialog
        if (activeDialogScope!!.isCompleted()) {
            dialogOfActiveScope.onComplete()
        } else {
            dialogOfActiveScope.onCancel()
        }
        activeDialogScope = null
    }

}