function resample
x=100:50:1100;
x=x/15000;
y=[0.76517408,0.77960199,0.783457705,0.803731,0.84054723,0.8328358,0.84154227,0.842537,0.85584575,0.854726,0.86268655,0.858706,0.85708955,0.862189,0.8600746,0.858208,0.8628107,0.86920398,0.871649735,0.873184,0.8716667];
plot(x,y,'-k*');
xlabel('Resample Ratio');
ylabel('Average Testing Accuracy');
title('Resample Ratio and Testing Accuracy for the Sample Set of Magic04');
