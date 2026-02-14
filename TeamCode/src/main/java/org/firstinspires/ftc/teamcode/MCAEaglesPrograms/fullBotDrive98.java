package org.firstinspires.ftc.teamcode.MCAEaglesPrograms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Full Bot Drive v2 98", group = "MCA EAGLES Programs")
public class fullBotDrive98 extends LinearOpMode {

    double speedFactor = 0.5;

    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotor intakeDirect, intakeBoost;
    DcMotor turret, flywheel;

    Servo pusher, blocker;

    // Timed pusher control
    ElapsedTime pusherTimer = new ElapsedTime();
    boolean pusherActive = false;

    @Override
    public void runOpMode() throws InterruptedException {

        frontLeft = hardwareMap.dcMotor.get("Front_Left");
        frontRight = hardwareMap.dcMotor.get("Front_Right");
        backLeft = hardwareMap.dcMotor.get("Back_Left");
        backRight = hardwareMap.dcMotor.get("Back_Right");
        intakeDirect = hardwareMap.dcMotor.get("Intake_Direct");
        intakeBoost = hardwareMap.dcMotor.get("Intake_Boost");
        flywheel = hardwareMap.dcMotor.get("Flywheel");
        turret = hardwareMap.dcMotor.get("Turret");

        pusher = hardwareMap.servo.get("Pusher");
        blocker = hardwareMap.servo.get("Blocker");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeDirect.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeBoost.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            // ================= DRIVE =================
            double y = -gamepad1.left_stick_y;   // forward/back
            double x = gamepad1.left_stick_x;    // strafe
            double rx = gamepad1.right_stick_x;  // turn

            frontLeft.setPower((y + x + rx) * speedFactor);
            backLeft.setPower((y - x + rx) * speedFactor);
            frontRight.setPower((y - x - rx) * speedFactor);
            backRight.setPower((y + x - rx) * speedFactor);


            // ================= INTAKE =================
            intakeDirect.setPower(-gamepad2.left_stick_y * speedFactor);
            intakeBoost.setPower(-intakeDirect.getPower());

            // ================= TURRET & FLYWHEEL =================
            turret.setPower(-gamepad2.right_stick_x * 0.3);

            if (gamepad2.left_trigger - gamepad2.right_trigger > 0.98) {
                flywheel.setPower(0.98);
            } else {
                flywheel.setPower(gamepad2.left_trigger - gamepad2.right_trigger);
            }


            // ================= BLOCKER =================
            if (gamepad2.rightBumperWasReleased()) {
                blocker.setPosition(0.29);
            } else if (gamepad2.leftBumperWasReleased()) {
                blocker.setPosition(0.39);
            }

            // ================= PUSHER (TIMED) =================
            if (gamepad2.dpadUpWasReleased()) {
                // Move UP immediately
                pusher.setPosition(0.3);
                pusherTimer.reset();
                pusherActive = true;
            }

            // After 1 second, move DOWN
            if (pusherActive && pusherTimer.seconds() >= 1.0) {
                pusher.setPosition(0.89);
                pusherActive = false;
            }
        }
    }
}

