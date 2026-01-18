package org.firstinspires.ftc.teamcode.MCAEaglesPrograms;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Full Bot Drive", group = "MCA EAGLES Programs")
public class fullBotDrive extends LinearOpMode {

    // Speed scaling
    double speedFactor = 1.0;

    // Hardware
    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotor intakeDirect, intakeBoost, turret, flywheel;
    Servo pusher, blocker;
    Limelight3A limelight;

    // Turret limits
    private final int TURRET_MAX = 1600;
    private final int TURRET_MIN = -1600;
    private final double TURRET_SPEED = 0.3;

    @Override
    public void runOpMode() throws InterruptedException {

        // Map hardware
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

        // Set motor behaviors
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

        // Reset turret encoder to zero (center)
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // ---- Drive ----
            frontLeft.setPower((-gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x) * speedFactor);
            backLeft.setPower((-gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x) * speedFactor);
            frontRight.setPower((-gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x) * speedFactor);
            backRight.setPower((-gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x) * speedFactor);

            // ---- Intake ----
            intakeDirect.setPower(-gamepad2.left_stick_y * speedFactor);
            intakeBoost.setPower(-intakeDirect.getPower());

            // ---- Turret with limits ----
            double turretInput = -gamepad2.right_stick_x * TURRET_SPEED;
            int currentPos = turret.getCurrentPosition();

            // Apply limits
            if ((currentPos >= TURRET_MAX && turretInput > 0) || (currentPos <= TURRET_MIN && turretInput < 0)) {
                turret.setPower(0);
            } else {
                turret.setPower(turretInput);
            }

            // ---- Flywheel ----
            flywheel.setPower(gamepad2.left_trigger - gamepad2.right_trigger);

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

            // ---- Telemetry ----
            telemetry.addData("Turret Power", turret.getPower());
            telemetry.addData("Turret Position", turret.getCurrentPosition());
            telemetry.addData("Turret Limits", "%d to %d", TURRET_MIN, TURRET_MAX);
            telemetry.addData("Flywheel Power", flywheel.getPower());
            telemetry.addData("Intake Power", intakeDirect.getPower());
            telemetry.addData("Pusher Pos", pusher.getPosition());
            telemetry.addData("Blocker Pos", blocker.getPosition());
            telemetry.update();
        }
    }
}
