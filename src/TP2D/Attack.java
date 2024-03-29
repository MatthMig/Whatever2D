package TP2D;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Attack extends AnimatedThings {
    public boolean isAttacking = false;
    private int attackAnimationFrame = 0;
    private final int numberOfAttackFrames = 9;  // Adjust this based on your actual number of frames
    private Image[] attackAnimationFrameImage = new Image[numberOfAttackFrames];
    private Timer attackTimer;

    public Attack(int width, int height) {
        super(120, 120, width, height);

        // Load attack animation frames here
        try {
            for (int i = 1; i <= numberOfAttackFrames; i++) {
                attackAnimationFrameImage[i-1] = ImageIO.read(new File("img/slash2/slash2_" + String.format("%05d", i) + ".png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize the attackTimer
        attackTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attackAnimationFrame++;
                if (attackAnimationFrame >= numberOfAttackFrames) {
                    stopAttack();
                }
            }
        });
    }

    public void startAttack() {
        if (!isAttacking) {
            isAttacking = true;
            attackAnimationFrame = 0;
            attackTimer.start();
        }
    }

    public void startAttack(ArrayList<DynamicThings> possibleTargets, double x, double y) {
        if (!isAttacking) {
            isAttacking = true;
            attackAnimationFrame = 0;
            attackTimer.start();
            this.x = x;
            this.y = y;

            HitBox attackHitBox = new HitBox(this.x, this.y, this.width, this.height);
            for (DynamicThings possibleTarget : possibleTargets) {
                if (attackHitBox.intersect(possibleTarget.getHitBox())) {
                    possibleTarget.takeDamage(10);
                }
            }
        }
    }

    private void stopAttack() {
        isAttacking = false;
        attackTimer.stop();
        attackAnimationFrame = 0;
    }

    @Override
    public void draw(Graphics g) {
        int attackIndex = attackAnimationFrame % numberOfAttackFrames;
        g.drawImage(
            attackAnimationFrameImage[attackIndex],
            (int) x, (int) y,
            (int) x + width, (int) y + height,  // Destination rectangle
            0, 0, attackAnimationFrameImage[attackIndex].getWidth(null), attackAnimationFrameImage[attackIndex].getHeight(null),  // Source rectangle (full image)
            null
        );
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
