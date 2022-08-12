package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */
@TeleOp(group = "drive")
public class Driving extends OpMode {

    ElapsedTime runtime = new ElapsedTime();

    DcMotorEx left = null;
    DcMotorEx right = null;
    DcMotorEx plate = null;
    DcMotorEx sling = null;
    Servo front = null;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotorEx.class, "left");
        right = hardwareMap.get(DcMotorEx.class, "right");
        plate = hardwareMap.get(DcMotorEx.class, "plate");
        sling = hardwareMap.get(DcMotorEx.class, "sling");
        front = hardwareMap.get(Servo.class, "servo");

        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        plate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sling.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sling.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        right.setDirection(DcMotorSimple.Direction.REVERSE);

        front.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void start() {
        plate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front.setPosition(0);

        runtime.reset();
    }

    @Override
    public void loop() {
        float sp = gamepad1.right_bumper ? 0.4f : 1f;
        float ms = 0.8f;

        float leftPower = (-gamepad1.left_trigger + gamepad1.right_trigger - gamepad1.left_stick_y) * sp * ms;
        float rightPower = (gamepad1.left_trigger - gamepad1.right_trigger - gamepad1.left_stick_y) * sp * ms;

        //float leftPower = (gamepad1.left_stick_x - gamepad1.left_stick_y) * sp * ms;
        //float rightPower = (-gamepad1.left_stick_x - gamepad1.left_stick_y) * sp * ms;

        debugTelemetry();

        if (gamepad1.a) upPlate();
        else downPlate();

        left.setPower(leftPower);
        right.setPower(rightPower);

        if(gamepad1.dpad_up)
            sling.setPower(.5f);
        else
            sling.setPower(0.01f);
    }

    boolean waitingLift = false, lifted = false;

    private void upPlate() {
        front.setPosition(0.15);
        if (!waitingLift && !lifted) {
            waitingLift = true;
            runtime.reset();
        }
        if (waitingLift && runtime.milliseconds() >= 300 && !lifted)
            waitingLift = false;
        if (!waitingLift) {
            lifted = true;
            plate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if (plate.getCurrentPosition() < 69)
                plate.setPower(0.5);
            else plate.setPower(0);
        }
    }

    private void downPlate() {
        lifted = false;
        front.setPosition(0);

        plate.setTargetPosition(-5);
        plate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        plate.setPower(1);
    }

    private void debugTelemetry() {
        telemetry.addData("roata stanga", left.getCurrentPosition());
        telemetry.addData("roata dreapta", right.getCurrentPosition());
        telemetry.addData("xleftstick", gamepad1.left_stick_x);
        telemetry.addData("yleftstick", gamepad1.left_stick_y);
        telemetry.addData("platepos", plate.getCurrentPosition());
        telemetry.addData("front", front.getPosition());
        telemetry.addData("planeta", sling.getCurrentPosition());
        telemetry.update();
    }

}
