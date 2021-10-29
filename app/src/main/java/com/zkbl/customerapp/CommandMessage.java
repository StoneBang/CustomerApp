package com.zkbl.customerapp;

public class CommandMessage {
    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String command = "";//命令
    public String commandId;//命令Id
    public String mode;//单标签，多标签

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int power;
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
