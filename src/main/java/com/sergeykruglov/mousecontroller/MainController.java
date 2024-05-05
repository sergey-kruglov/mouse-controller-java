package com.sergeykruglov.MouseController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.awt.event.InputEvent;

@Controller
@Slf4j
public class MainController {
    static {
        System.setProperty("java.awt.headless", "false");
    }

    Robot robot;

    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping("/click")
    public void click() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    @MessageMapping("/touchmove")
    public void touchMove(@Payload String coordinates) {
        if (coordinates == null) return;

        String[] cParts = coordinates.split(";");
        int diffX, diffY;

        try {
            diffX = Integer.parseInt(cParts[0]);
            diffY = Integer.parseInt(cParts[1]);
        } catch (NumberFormatException e) {
            log.error("Invalid integer input");
            return;
        }

        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        Point point = pointerInfo.getLocation();

        int x = point.x + diffX;
        int y = point.y + diffY;
        robot.mouseMove(x, y);
    }
}
