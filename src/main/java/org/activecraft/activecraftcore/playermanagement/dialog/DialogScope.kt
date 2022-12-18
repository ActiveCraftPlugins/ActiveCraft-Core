package org.activecraft.activecraftcore.playermanagement.dialog

import java.util.*

class DialogScope(val dialogManager: DialogManager, val dialog: Dialog) {

    val dialogSteps: Queue<DialogStep> = LinkedList(dialog.dialogSteps)
    var isPaused = false
        private set

    fun isCompleted() = dialogSteps.peek() == null

    fun pause() {
        isPaused = true
    }

    fun resume() {
        isPaused = false
    }

    @Throws(IllegalStateException::class)
    fun next() {
        val profile = dialogManager.profile
        if (isPaused)
            throw IllegalStateException("Dialog is paused.")
        if (!profile.isOnline)
            dialogManager.closeDialog()
        dialogSteps.poll()
        if (isCompleted())
            return dialogManager.closeDialog()
        sendActiveDialogStepMessage()
    }

    fun skip() {
        dialogSteps.poll()
    }

    fun sendActiveDialogStepMessage() {
        val profile = dialogManager.profile
        profile.player!!.sendMessage(dialogSteps.peek().message)
    }

    fun append(vararg dialogSteps: DialogStep) = dialogSteps.forEach { this.dialogSteps.offer(it) }

    @Throws(IllegalStateException::class)
    fun answer(answer: String) {
        if (isPaused)
            throw IllegalStateException("Dialog is paused.")
        val activeDialogStep = dialogSteps.peek()
        if (activeDialogStep.cancelKeywords.contains(answer))
            return dialogManager.closeDialog()
        activeDialogStep.onAnswer(this)
        next()
    }

}