package org.firstinspires.ftc.teamcode.MCAEaglesPrograms;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Full Bot Drive", group = "MCA EAGLES Programs")
public class fullBotDrive extends LinearOpMode {

    // Define the speedfactor variable to be used to control the max percent of speed.
    double speedFactor = 1.0;

    // Define all of the motors and servos.
    DcMotor frontLeft = null;
    DcMotor frontRight = null;
    DcMotor backLeft = null;
    DcMotor backRight = null;
    DcMotor intakeDirect = null;
    DcMotor intakeBoost = null;
    DcMotor turret = null;
    DcMotor flywheel = null;
    Servo pusher = null;
    Servo blocker = null;
    Limelight3A limelight = null;

    @Override
    public void runOpMode() throws InterruptedException {

        // Hardware map all of the motors.
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

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        // Set all of the motors to brake when not powered.
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeDirect.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeBoost.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Reverse the direction of some of the robot's motors.
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);

        // ---- Telemetry ----
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Controls", "Gamepad1: Drive | Gamepad2: Turret/Flywheel/Intake");
        telemetry.update();

        // Wait for the start button to be pushed before starting the run loop.
        waitForStart();

        while (opModeIsActive()) {

            // ---- Drive ----
            double flPower = (-gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x) * speedFactor;
            double blPower = (-gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x) * speedFactor;
            double frPower = (-gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x) * speedFactor;
            double brPower = (-gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x) * speedFactor;

            frontLeft.setPower(flPower);
            backLeft.setPower(blPower);
            frontRight.setPower(frPower);
            backRight.setPower(brPower);

            // ---- Intake ----
            double intakePower = -gamepad2.left_stick_y * speedFactor;
            intakeDirect.setPower(intakePower);
            intakeBoost.setPower(-intakeDirect.getPower());

            // ---- Turret / Flywheel ----
            double turretPower = -gamepad2.right_stick_x * 0.3;
            turret.setPower(turretPower);
            double flywheelPower = gamepad2.left_trigger - gamepad2.right_trigger;
            flywheel.setPower(flywheelPower);

            // ---- Servos ----
            if (gamepad2.rightBumperWasReleased()) {
                blocker.setPosition(0.29);
            } else if (gamepad2.leftBumperWasReleased()) {
                blocker.setPosition(0.39);
            }

            if (gamepad2.dpadDownWasReleased()) {
                pusher.setPosition(0.2);
            } else if (gamepad2.dpadUpWasReleased()) {
                pusher.setPosition(0.0);
            }

            // ---- Telemetry Display ----
            telemetry.addData("Drive FL/FR/BL/BR", "%.2f %.2f %.2f %.2f", flPower, frPower, blPower, brPower);
            telemetry.addData("Intake Pwr", "%.2f", intakePower);
            telemetry.addData("Turret Pwr", "%.2f", turretPower);
            telemetry.addData("Flywheel Pwr", "%.2f", flywheelPower);
            telemetry.addData("Pusher Pos", "%.2f", pusher.getPosition());
            telemetry.addData("Blocker Pos", "%.2f", blocker.getPosition());

            if (limelight != null) {
                telemetry.addData("Limelight", "Active");
                // Optionally, you can add more Limelight info here if needed
                // e.g., target angles, distances, or number of fiducials
            }

            telemetry.update();
        }
    }
}
