function cart_delve_sizeup
   time_6=[71.4278295,118.4833115,197.025593,264.548217,325.220485,407.1552235,553.8562975,692.459126,802.542221,892.097796];
   time_8=[69.650916,89.190205,114.5773185,190.2045095,259.969429,445.0550555,539.5374385,676.818347,775.572673,793.590343];
   for i=1:10
       sizeup_6(i)=time_6(i)/time_6(1);
       sizeup_8(i)=time_8(i)/time_8(1);
   end
   m=1:1:10;
   plot(m,sizeup_6,'-o',m,sizeup_8,'--*');
   xlabel('m');
   ylabel('sizeup');
   title('cart delve');
   axis([1 10 1 15]);
   axis normal;
   legend('6 nodes','8 nodes');
end